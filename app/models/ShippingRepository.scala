package models

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ShippingRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._
  class ShippingTable(tag: Tag) extends Table[Shipping](tag, "shipping") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def street = column[String]("street")
    def city = column[String]("city")
    def house = column[Int]("house")
    def phone = column[Int]("phone")

    def * = (id, street,city,house,phone) <> ((Shipping.apply _).tupled, Shipping.unapply)
  }

  val shipping = TableQuery[ShippingTable]

  def create(street: String,city: String,house: Int,phone: Int): Future[Shipping] = db.run {
    (shipping.map(c => (c.street, c.city, c.house, c.phone))
      returning shipping.map(_.id)

    into {case ((street,city,house,phone),id) => Shipping(id,street,city,house,phone)}

    ) += (street,city,house,phone)

  }

  def list(): Future[Seq[Shipping]] = db.run {
    shipping.result
  }

  def getById(id: Int): Future[Option[Shipping]] = db.run {
    shipping.filter(_.id === id).result.headOption
  }


  def delete(id: Int): Future[Int] = db.run(shipping.filter(_.id === id).delete)

  def update(id: Int, newShipping: Shipping): Future[Int] = {
    val shippingToUpdate: Shipping = newShipping.copy(id)
    db.run(shipping.filter(_.id === id).update(shippingToUpdate))
  }
}