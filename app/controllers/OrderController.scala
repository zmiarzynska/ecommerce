package controllers

import models.{Order, OrderRepository}
import play.api.mvc._
import play.api.libs.json.{JsValue, Json}

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class OrderController @Inject()(cc: ControllerComponents,
                                orderRepository: OrderRepository,
                               )(implicit ec: ExecutionContext) extends AbstractController(cc) {


  def createMyOrders(): Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Order].map {
        order =>
          orderRepository.create(order.paymentId).map { res =>
            Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("")))
  }


  def getMyOrders(): Action[AnyContent] = Action.async {
    val orders = orderRepository.list()
    orders.map {
      orders => Ok(Json.toJson(orders))
    }
  }

  def readMyOrders(id: Int): Action[AnyContent] = Action.async {
    val order = orderRepository.getByIdOption(id)
    order.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("")
    }
  }

  def updateOrder(id: Int): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Order].map {
      order =>
        orderRepository.update(order.id, order).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("")))
  }

  def deleteOrder(id: Int): Action[AnyContent] = Action.async {
    orderRepository.delete(id).map { res =>
      Ok(Json.toJson(res))
    }
  }


}