package controllers

import models.{Shipping, ShippingRepository}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class ShippingController @Inject()(cc: ControllerComponents,
                                   shippingRepository: ShippingRepository
                                  )(implicit ec: ExecutionContext) extends AbstractController(cc) {


  def createShipping(): Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Shipping].map {
        shipping =>
          shippingRepository.create(shipping.street, shipping.city, shipping.house, shipping.phone).map { res =>
            Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("")))
  }


  def readAllShippings(): Action[AnyContent] = Action.async {
    val shippings = shippingRepository.list()
    shippings.map {
      shippings => Ok(Json.toJson(shippings))
    }
  }

  def readShipping(id: Int): Action[AnyContent] = Action.async {
    val shipping = shippingRepository.getById(id)
    shipping.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("")
    }
  }

  def updateShipping(id: Int): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Shipping].map {
      shipping =>
        shippingRepository.update(shipping.id, shipping).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("")))
  }

  def deleteShipping(id: Int): Action[AnyContent] = Action.async {
    shippingRepository.delete(id).map { res =>
      Ok(Json.toJson(res))
    }
  }


}