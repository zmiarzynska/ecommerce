package models

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RateRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, userRepository: UserRepository)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._


  private class RateTable(tag: Tag) extends Table[Rate](tag, "rate") {
   /* Rate(id: Int, amount: Int, description: String, username_id: Int) */

    /** The ID column, which is the primary key, and auto incremented */
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    /** The name column */
    def amount = column[Int]("amount")

    /** The age column */
    def description = column[String]("description")

    def username_id = column[Int]("username_id")

    def username_id_fk = foreignKey("username_id_fk",username_id, usr)(_.id)


    /**
     * This is the tables default "projection".
     *
     * It defines how the columns are converted to and from the Person object.
     *
     * In this case, we are simply passing the id, name and page parameters to the Person case classes
     * apply and unapply methods.
     */
    def * = (id, amount, description, username_id) <> ((Rate.apply _).tupled, Rate.unapply)

  }

  /**
   * The starting point for all queries on the people table.
   */
  import userRepository.UserTable


  private val rate = TableQuery[RateTable]

  private val usr = TableQuery[UserTable]


  /**
   * Create a person with the given name and age.
   *
   * This is an asynchronous operation, it will return a future of the created person, which can be used to obtain the
   * id for that person.
   */
  def create(amount: Int, description: String, username_id: Int): Future[Rate] = db.run {
    // We create a projection of just the name and age columns, since we're not inserting a value for the id column
    (rate.map(p => (p.amount, p.description,p.username_id))
      // Now define it to return the id, because we want to know what id was generated for the person
      returning rate.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into {case ((amount,description,username_id),id) => Rate(id,amount, description,username_id)}
      // And finally, insert the rate into the database
      ) += (amount, description,username_id)
  }

  /**
   * List all the people in the database.
   */
  def list(): Future[Seq[Rate]] = db.run {
    rate.result
  }


  def getById(id: Int): Future[Rate] = db.run {
    rate.filter(_.id === id).result.head
  }


  def delete(id: Int): Future[Unit] = db.run(rate.filter(_.id === id).delete).map(_ => ())

  def update(id: Int, new_rate: Rate): Future[Unit] = {
    val rateToUpdate: Rate = new_rate.copy(id)
    db.run(rate.filter(_.id === id).update(rateToUpdate)).map(_ => ())
  }

}