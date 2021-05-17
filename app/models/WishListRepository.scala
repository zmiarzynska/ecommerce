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

    def username_id = column[Int]("username_id")

    def username_id_fk = foreignKey("username_id_fk", username_id, usr)(_.id)

    def item_id = column[Int]("item_id")

  /*  def item_id_fk = foreignKey("item_id_fk", item_id, item)(_.id) */


    /**
     * This is the tables default "projection".
     *
     * It defines how the columns are converted to and from the Person object.
     *
     * In this case, we are simply passing the id, name and page parameters to the Person case classes
     * apply and unapply methods.
     */
    def * = (id, username_id, item_id) <> ((WishList.apply _).tupled, WishList.unapply)

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
  def create(username_id: Int, item_id: Int): Future[WishList] = db.run {
    // We create a projection of just the name and age columns, since we're not inserting a value for the id column
    (wishList.map(p => (p.username_id, p.item_id))
      // Now define it to return the id, because we want to know what id was for the person
      returning wishList.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into { case ((username_id, item_id), id) => WishList(id, username_id, item_id) }
      // And finally, insert the wishList into the database
      ) += (username_id,item_id)
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


  def delete(id: Int): Future[Unit] = db.run(wishList.filter(_.id === id).delete).map(_ => ())

  def update(id: Int, new_wishList: WishList): Future[Unit] = {
    val wishListToUpdate: WishList = new_wishList.copy(id)
    db.run(wishList.filter(_.id === id).update(wishListToUpdate)).map(_ => ())
  }

}