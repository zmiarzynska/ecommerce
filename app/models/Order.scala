package models

import play.api.libs.json.Json

case class Order(id: Int, paymentId: Int)

object Order{
  implicit val orderFormat = Json.format[Order]
}