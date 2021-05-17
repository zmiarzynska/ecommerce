package models

import play.api.libs.json.Json

case class WishList(id: Int, username_id: Int, item_id: Int)


object WishList{
  implicit val wishListFormat = Json.format[WishList]
}