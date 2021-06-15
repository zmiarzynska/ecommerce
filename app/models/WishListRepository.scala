package models

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class WishListRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, userRepository: UserRepository,
                                   itemRepository: ItemRepository)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class WishListTable(tag: Tag) extends Table[WishList](tag, "wishList") {

    /** The ID column, which is the primary key, and auto incremented */
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def usernameId = column[Int]("usernameId")

    def usernameIdFk = foreignKey("usernameIdFk", usernameId, usr)(_.id)

    def itemId = column[Int]("itemId")
    

    /**
     * This is the tables default "projection".
     *
     * It defines how the columns are converted to and from the Person object.
     *
     * In this case, we are simply passing the id, name and page parameters to the Person case classes
     * apply and unapply methods.
     */
    def * = (id, usernameId, itemId) <> ((WishList.apply _).tupled, WishList.unapply)

  }

  /**
   * The starting point for all queries on the people table.
   */

  import userRepository.UserTable
  import itemRepository.ItemTable

  private val wishList = TableQuery[WishListTable]

  private val usr = TableQuery[UserTable]
 /* private val item = TableQuery[ItemTable]*/



  /**
   * Create a person with the given name and age.
   *
   * This is an asynchronous operation, it will return a future of the created person, which can be used to obtain the
   * id for that person.
   */
  def create(usernameId: Int, itemId: Int): Future[WishList] = db.run {
    // We create a projection of just the name and age columns, since we're not inserting a value for the id column
    (wishList.map(p => (p.usernameId, p.itemId))
      // Now define it to return the id, because we want to know what id was for the person
      returning wishList.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into { case ((usernameId, itemId), id) => WishList(id, usernameId, itemId) }
      // And finally, insert the wishList into the database
      ) += (usernameId,itemId)
  }

  /**
   * List all the people in the database.
   */
  def list(): Future[Seq[WishList]] = db.run {
    wishList.result
  }


  def getById(id: Int): Future[WishList] = db.run {
    wishList.filter(_.id === id).result.head
  }

  def getByIdOption(id: Int): Future[Option[WishList]] = db.run {
    wishList.filter(_.id === id).result.headOption
  }

  def delete(id: Int): Future[Int] = db.run(wishList.filter(_.id === id).delete)

  def update(id: Int, newWishList: WishList): Future[Int] = {
    val wishListToUpdate: WishList = newWishList.copy(id)
    db.run(wishList.filter(_.id === id).update(wishListToUpdate))  }

}