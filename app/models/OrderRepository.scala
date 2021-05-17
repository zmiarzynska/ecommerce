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
    /* Order(id: Int, amount: Int, description: String, payment_id: Int) */

    /** The ID column, which is the primary key, and auto incremented */
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def payment_id = column[Int]("payment")
    def payment_id_fk = foreignKey("payment_id_fk", payment_id, usr)(_.id)


    /**
     * This is the tables default "projection".
     *
     * It defines how the columns are converted to and from the Person object.
     *
     * In this case, we are simply passing the id, name and page parameters to the Person case classes
     * apply and unapply methods.
     */
    def * = (id, payment_id) <> ((Order.apply _).tupled, Order.unapply)

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
  def create(payment_id: Int): Future[Order] = db.run {
    // We create a projection of just the name and age columns, since we're not inserting a value for the id column
    (order.map(p => (p.payment_id))
      // Now define it to return the id, because we want to know what id was geneorderd for the person
      returning order.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into { case ((payment_id), id) => Order(id, payment_id) }
      // And finally, insert the order into the database
      ) += (payment_id)
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

  def delete(id: Int): Future[Unit] = db.run(order.filter(_.id === id).delete).map(_ => ())

  def update(id: Int, new_order: Order): Future[Unit] = {
    val orderToUpdate: Order = new_order.copy(id)
    db.run(order.filter(_.id === id).update(orderToUpdate)).map(_ => ())
  }

}