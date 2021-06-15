package models

import play.api.libs.json.Json


case class Payment (id: Int, amount: Int, paymentType: Int)

object Payment {
  implicit val paymentFormat = Json.format[Payment]
}
