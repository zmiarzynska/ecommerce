package models

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig._
  import profile.api._


   class UserTable(tag: Tag) extends Table[User](tag, "user") {

    /** The ID column, which is the primary key, and auto incremented */
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    /*case class User(id: Int, username: String, password: String) */

    /** The name column */
    def username = column[String]("username")

    def password = column[String]("password")


    /**
     * This is the tables default "projection".
     *
     * It defines how the columns are converted to and from the Person object.
     *
     * In this case, we are simply passing the id, name and page parameters to the Person case classes
     * apply and unapply methods.
     */
    def * = (id, username, password ) <> ((User.apply _).tupled, User.unapply)

  }

  /**
   * The starting point for all queries on the people table.
   */


   val user = TableQuery[UserTable]

  /**
   * Create a person with the given name and age.
   *
   * This is an asynchronous operation, it will return a future of the created person, which can be used to obtain the
   * id for that person.
   */
  def create(username: String, password: String): Future[User] = db.run {
    // We create a projection of just the name and age columns, since we're not inserting a value for the id column
    (user.map(p => (p.username, p.password))
      // Now define it to return the id, because we want to know what id was generated for the person
      returning user.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into {case ((username,password),id) => User(id,username, password)}
      // And finally, insert the user into the database
      ) += (username,password)
  }

  /**
   * List all the people in the database.
   */
  def list(): Future[Seq[User]] = db.run {
    user.result
  }



  def getById(id: Int): Future[User] = db.run {
    user.filter(_.id === id).result.head
  }

  def getByIdOption(id: Int): Future[Option[User]] = db.run {
    user.filter(_.id === id).result.headOption
  }

  def delete(id: Int): Future[Int] = db.run(user.filter(_.id === id).delete)

  def update(id: Int, new_user: User): Future[Int] = {
    val userToUpdate: User = new_user.copy(id)
    db.run(user.filter(_.id === id).update(userToUpdate))
  }

}