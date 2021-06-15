package controllers.forms

import models.{Category, Order}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import models.{CategoryRepository, OrderRepository}

import javax.inject._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class OrderController @Inject()(orderRepo: OrderRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val orderUrl = "/forms/orders"
  def listOrders(): Action[AnyContent] = Action.async { implicit request =>
    orderRepo.list().map(orders => Ok(views.html.list_orders(orders)))
  }

  def createOrder(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

    val orders = orderRepo.list()
    orders.map(_ => Ok(views.html.create_order(orderForm)))
  }

  def createOrderHandle(): Action[AnyContent] = Action.async { implicit request =>
    orderForm.bindFromRequest.fold(
      errorForm => {
        println("bad request")
        Future.successful(
          BadRequest(views.html.create_order(errorForm))
        )
      },
      order => {
        orderRepo.create(order.paymentId).map { _ =>
          Redirect(orderUrl)
        }
      }
    )
  }

  def updateOrder(id: Int): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val order = orderRepo.getByIdOption(id)
    order.map(order => {
      val prodForm = updateOrderForm.fill(UpdateOrderForm(order.get.id, order.get.paymentId))
      Ok(views.html.update_order(prodForm))
    })
  }

  def updateOrderHandle(): Action[AnyContent] = Action.async { implicit request =>
    updateOrderForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.update_order(errorForm))
        )
      },
      order => {
        orderRepo.update(order.id, Order(order.id, order.paymentId)).map { _ =>
          Redirect(orderUrl)
        }
      }
    )
  }

  def deleteOrder(id: Int): Action[AnyContent] = Action {
    orderRepo.delete(id)
    Redirect(orderUrl)
  }

  // utilities

  val orderForm: Form[CreateOrderForm] = Form {
    mapping(
      "paymentId" -> number
    )(CreateOrderForm.apply)(CreateOrderForm.unapply)
  }

  val updateOrderForm: Form[UpdateOrderForm] = Form {
    mapping(
      "id" -> number,
      "paymentId" -> number
    )(UpdateOrderForm.apply)(UpdateOrderForm.unapply)
  }


}

case class CreateOrderForm(paymentId: Int)
case class UpdateOrderForm(id: Int, paymentId: Int)
