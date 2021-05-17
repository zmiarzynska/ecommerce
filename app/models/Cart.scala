package models

import play.api.libs.json.Json

case class Cart (id: Int, number: Int)

object Cart {
  implicit val categoryFormat = Json.format[Cart]
}
