package models

import play.api.libs.json.Json

case class User(id: Int, username: String, password: String)

object User{
  implicit val userFormat = Json.format[User]
}

