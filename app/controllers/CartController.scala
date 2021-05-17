package controllers
import models.{Cart, CartRepository}
import play.api.mvc._
import play.api.libs.json.{JsValue, Json}
import javax.inject._
import scala.concurrent.{Future,ExecutionContext}


@Singleton
class CartController @Inject()(cc: ControllerComponents,
                              val cartRepository: CartRepository)
                              (implicit ec: ExecutionContext)
                              extends AbstractController(cc) {


  def createCart(): Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Cart].map {
        cart =>
          cartRepository.create(cart.id).map { res =>
            Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("")))
  }


  def readCart(id: Int): Action[AnyContent] = Action.async {
    val carts = cartRepository.getById(id)
    carts.map {
      carts => Ok(Json.toJson(carts))
    }
  }

  def readAllCarts(): Action[AnyContent] = Action.async {
    val carts = cartRepository.list()
    carts.map {
      carts => Ok(Json.toJson(carts))
    }
  }

  def updateCart(id: Int): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Cart].map {
      cart =>
        cartRepository.update(cart.id, cart).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("")))
  }

  def deleteCart(id: Int): Action[AnyContent] = Action.async {
    cartRepository.delete(id).map { res =>
      Ok(Json.toJson(res))
    }
  }

}