package controllers

import models.{Payment, PaymentRepository}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class PaymentController @Inject()(cc: ControllerComponents,
                                  paymentRepository: PaymentRepository,
                                 ) (implicit ec: ExecutionContext) extends AbstractController(cc) {


  def createPayment(): Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Payment].map {
        payment =>
          paymentRepository.create(payment.amount, payment.payment_type).map { res =>
            Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("")))
  }


  def getPayments(): Action[AnyContent] = Action.async {
    val payments = paymentRepository.list()
    payments.map {
      payments => Ok(Json.toJson(payments))
    }
  }

  def readPayment(id: Int): Action[AnyContent] = Action.async {
    val payment = paymentRepository.getById(id)
    payment.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("")
    }
  }

  def updatePayment(id: Int): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Payment].map {
      payment =>
        paymentRepository.update(payment.id, payment).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("")))
  }

  def deletePayment(id: Int): Action[AnyContent] = Action.async {
    paymentRepository.delete(id).map { res =>
      Ok(Json.toJson(res))
    }
  }
}