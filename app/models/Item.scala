package models

import play.api.libs.json.Json

case class Item(id: Int, name: String, description: String, category: Int)

object Item {
  implicit val itemFormat = Json.format[Item]
}