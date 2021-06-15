package models

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ Future, ExecutionContext }

@Singleton
class AccountRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._


  private class AccountTable(tag: Tag) extends Table[Account](tag, "account") {

    /** The ID column, which is the primary key, and auto incremented */
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    /** The name column */
    def firstName = column[String]("firstName")

    def lastName = column[String]("lastName")


    /** The age column */
    def city = column[String]("city")


    /**
     * This is the tables default "projection".
     *
     * It defines how the columns are converted to and from the Person object.
     *
     * In this case, we are simply passing the id, name and page parameters to the Person case classes
     * apply and unapply methods.
     */
    def * = (id, firstName, lastName, city ) <> ((Account.apply _).tupled, Account.unapply)

  }

  /**
   * The starting point for all queries on the people table.
   */


  private val account = TableQuery[AccountTable]

  /**
   * Create a person with the given name and age.
   *
   * This is an asynchronous operation, it will return a future of the created person, which can be used to obtain the
   * id for that person.
   */
  def create(firstName: String, lastName: String, city: String): Future[Account] = db.run {
    // We create a projection of just the name and age columns, since we're not inserting a value for the id column
    (account.map(p => (p.firstName, p.lastName, p.city))
      // Now define it to return the id, because we want to know what id was generated for the person
      returning account.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into {case ((firstName,lastName, city),id) => Account(id,firstName, lastName, city)}
      // And finally, insert the account into the database
      ) += (firstName,lastName,city)
  }

  /**
   * List all the people in the database.
   */
  def list(): Future[Seq[Account]] = db.run {
    account.result
  }



  def getById(id: Int): Future[Account] = db.run {
    account.filter(_.id === id).result.head
  }

  def getByIdOption(id: Int): Future[Option[Account]] = db.run {
    account.filter(_.id === id).result.headOption
  }

  def delete(id: Int): Future[Int] = db.run(account.filter(_.id === id).delete)

  def update(id: Int, newAccount: Account): Future[Int] = {
    val accountToUpdate: Account = newAccount.copy(id)
    db.run(account.filter(_.id === id).update(accountToUpdate))
  }

}