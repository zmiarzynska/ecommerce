package models

import play.api.libs.json.Json

case class Account(id: Int, firstName: String, lastName: String, city: String)

object Account {
  implicit val accountFormat = Json.format[Account]
}