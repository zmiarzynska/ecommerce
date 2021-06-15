package models

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PaymentRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class PaymentTable(tag: Tag) extends Table[Payment](tag, "payment") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def amount = column[Int]("amount")
    def paymentType = column[Int]("paymentType")
    def * = (id, amount, paymentType) <> ((Payment.apply _).tupled, Payment.unapply)
  }

  val payment = TableQuery[PaymentTable]

  def create(amount: Int, paymentType: Int): Future[Payment] = db.run {
    (payment.map(c => (c.amount, c.paymentType))
      returning payment.map(_.id)

    into {case ((amount,paymentType),id) => Payment(id, amount, paymentType)}
    ) += (amount, paymentType)
  }

  def list(): Future[Seq[Payment]] = db.run {
    payment.result
  }


  def getById(id: Int): Future[Option[Payment]] = db.run {
    payment.filter(_.id === id).result.headOption
  }

  def delete(id: Int): Future[Int] = db.run(payment.filter(_.id === id).delete)

  def update(id: Int, newPayment: Payment): Future[Int] = {
    val paymentToUpdate: Payment = newPayment.copy(id)
    db.run(payment.filter(_.id === id).update(paymentToUpdate))
  }
}