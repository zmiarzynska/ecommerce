package controllers

import models.{ItemRepository, UserRepository, WishList, WishListRepository}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class WishListController @Inject()(cc: ControllerComponents,
                                   wishlistRepository: WishListRepository,
                                ) (implicit ec: ExecutionContext)
  extends AbstractController(cc) {



  def createWishList: Action[JsValue] = Action.async(parse.json)  {
    implicit request =>
      request.body.validate[WishList].map {
        wishlist =>
          wishlistRepository.create(wishlist.username_id, wishlist.item_id).map { res =>
            Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("")))
  }

  def getWishlists: Action[AnyContent] = Action.async {
    val wishlists = wishlistRepository.list()
    wishlists.map{
      wishlists => Ok(Json.toJson(wishlists))
    }
  }


  def readWishList(id: Int): Action[AnyContent] = Action.async {
    val wishlist = wishlistRepository.getByIdOption(id)
    wishlist.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("")
    }
  }

  def updateWishList(id: Int): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[WishList].map {
      wishlist =>
        wishlistRepository.update(wishlist.id, wishlist).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("")))
  }

  def deleteWishList(id: Int): Action[AnyContent] = Action.async {
    wishlistRepository.delete(id).map { res =>
      Ok(Json.toJson(res))
    }
  }

}