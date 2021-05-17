package models

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CartRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class CartTable(tag: Tag) extends Table[Cart](tag, "cart") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def number = column[Int]("number")
    def * = (id, number) <> ((Cart.apply _).tupled, Cart.unapply)
  }

  val cart = TableQuery[CartTable]

  def create(number: Int): Future[Cart] = db.run {
    (cart.map(c => (c.number))
      returning cart.map(_.id)
      into ((number, id) => Cart(id, number))
      ) += (number)
  }

  def list(): Future[Seq[Cart]] = db.run {
    cart.result
  }

  def getById(id: Int): Future[Option[Cart]] = db.run {
    cart.filter(_.id === id).result.headOption
  }


  def update(id:Int, c: Cart): Future[Int] = {
    val updatedCart: Cart = c.copy(id)
    db.run(cart.filter(_.id === id).update(updatedCart))
  }

  def delete(id: Int): Future[Int] = db.run(cart.filter(_.id === id).delete)

}
