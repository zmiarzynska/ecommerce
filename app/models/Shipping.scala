package models

import play.api.libs.json.Json

case class Shipping (id: Int, street: String, city: String, house: Int, phone: Int)

object Shipping {
  implicit val shippingFormat = Json.format[Shipping]
}
