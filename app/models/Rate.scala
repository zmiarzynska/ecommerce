package models

import play.api.libs.json.Json

case class Rate(id: Int, amount: Int, description: String, usernameId: Int)

object Rate{
  implicit val rateFormat = Json.format[Rate]
}