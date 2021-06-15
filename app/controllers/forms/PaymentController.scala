package controllers.forms

import models.Payment
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import models.PaymentRepository

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class PaymentController @Inject()(paymentRepo: PaymentRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {
val paymentUrl = "/forms/payments"
  def listPayments: Action[AnyContent] = Action.async { implicit request =>
    paymentRepo.list().map(payments => Ok(views.html.list_payments(payments)))
  }

  def createPayment(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val payments = paymentRepo.list()
    payments.map(_ => Ok(views.html.create_payment(paymentForm)))
  }

  def createPaymentHandle(): Action[AnyContent] = Action.async { implicit request =>
    paymentForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.create_payment(errorForm))
        )
      },
      payment => {
        paymentRepo.create(payment.amount, payment.paymentType).map { _ =>
          Redirect(paymentUrl)
        }
      }
    )
  }

  def updatePayment(id: Int): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val payment = paymentRepo.getById(id)
    payment.map(payment => {
      val prodForm = updatePaymentForm.fill(UpdatePaymentForm(payment.get.id, payment.get.amount, payment.get.paymentType))
      Ok(views.html.update_payment(prodForm))
    })
  }

  def updatePaymentHandle(): Action[AnyContent] = Action.async { implicit request =>
    updatePaymentForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.update_payment(errorForm))
        )
      },
      payment => {
        paymentRepo.update(payment.id, Payment(payment.id, payment.amount, payment.paymentType)).map { _ =>
          Redirect(paymentUrl)
        }
      }
    )
  }

  def deletePayment(id: Int): Action[AnyContent] = Action {
    paymentRepo.delete(id)
    Redirect(paymentUrl)
  }


  val paymentForm: Form[CreatePaymentForm] = Form {
    mapping(
      "amount" -> number,
      "paymentType" -> number
    )(CreatePaymentForm.apply)(CreatePaymentForm.unapply)
  }

  val updatePaymentForm: Form[UpdatePaymentForm] = Form {
    mapping(
      "id" -> number,
      "amount" -> number,
      "paymentType" -> number
    )(UpdatePaymentForm.apply)(UpdatePaymentForm.unapply)
  }
}

case class CreatePaymentForm(amount: Int,paymentType: Int)
case class UpdatePaymentForm(id: Int, amount: Int, paymentType: Int)