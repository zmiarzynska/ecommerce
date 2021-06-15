package models

import play.api.libs.json.Json

case class WishList(id: Int, usernameId: Int, itemId: Int)


object WishList{
  implicit val wishListFormat = Json.format[WishList]
}