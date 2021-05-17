package models

import play.api.libs.json.Json

case class Account(id: Int, first_name: String, last_name: String, city: String)

object Account {
  implicit val accountFormat = Json.format[Account]
}