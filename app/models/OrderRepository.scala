package models

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, paymentRepository: PaymentRepository)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._


  private class OrderTable(tag: Tag) extends Table[Order](tag, "order") {

    /** The ID column, which is the primary key, and auto incremented */
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def paymentId = column[Int]("payment")
    def paymentIdFk = foreignKey("paymentIdFk", paymentId, usr)(_.id)


    /**
     * This is the tables default "projection".
     *
     * It defines how the columns are converted to and from the Person object.
     *
     * In this case, we are simply passing the id, name and page parameters to the Person case classes
     * apply and unapply methods.
     */
    def * = (id, paymentId) <> ((Order.apply _).tupled, Order.unapply)

  }

  /**
   * The starting point for all queries on the people table.
   */

  import paymentRepository.PaymentTable


  private val order = TableQuery[OrderTable]

  private val usr = TableQuery[PaymentTable]


  /**
   * Create a person with the given name and age.
   *
   * This is an asynchronous operation, it will return a future of the created person, which can be used to obtain the
   * id for that person.
   */
  def create(paymentId: Int): Future[Order] = db.run {
    // We create a projection of just the name and age columns, since we're not inserting a value for the id column
    (order.map(p => (p.paymentId))
      // Now define it to return the id, because we want to know what id was geneorderd for the person
      returning order.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into { case ((paymentId), id) => Order(id, paymentId) }
      // And finally, insert the order into the database
      ) += (paymentId)
  }

  /**
   * List all the people in the database.
   */
  def list(): Future[Seq[Order]] = db.run {
    order.result
  }


  def getById(id: Int): Future[Order] = db.run {
    order.filter(_.id === id).result.head
  }

  def getByIdOption(id: Int): Future[Option[Order]] = db.run {
    order.filter(_.id === id).result.headOption
  }

  def delete(id: Int): Future[Int] = db.run(order.filter(_.id === id).delete)

  def update(id: Int, newOrder: Order): Future[Int] = {
    val orderToUpdate: Order = newOrder.copy(id)
    db.run(order.filter(_.id === id).update(orderToUpdate))
  }

}