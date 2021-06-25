package models

import play.api.libs.json.Json

case class Item(id: Int, name: String, price: Int, description: String ,image: String, category: Int)

object Item {
  implicit val itemFormat = Json.format[Item]
}