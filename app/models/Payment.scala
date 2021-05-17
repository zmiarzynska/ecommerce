package models

import play.api.libs.json.Json


case class Payment (id: Int, amount: Int, payment_type: Int)

object Payment {
  implicit val paymentFormat = Json.format[Payment]
}
