package controllers.forms

import models.{Category, Rate}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import models.{CategoryRepository, RateRepository}

import javax.inject._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class RateController @Inject()(rateRepo: RateRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {


  def listRates(): Action[AnyContent] = Action.async { implicit request =>
    rateRepo.list().map(rates => Ok(views.html.list_rates(rates)))
  }

  def createRate(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

    val rates = rateRepo.list()
    rates.map(_ => Ok(views.html.create_rate(rateForm)))
  }

  def createRateHandle(): Action[AnyContent] = Action.async { implicit request =>
    rateForm.bindFromRequest.fold(
      errorForm => {
        println("bad request")
        Future.successful(
          BadRequest(views.html.create_rate(errorForm))
        )
      },
      rate => {
        rateRepo.create(rate.amount, rate.description,rate.username_id).map { _ =>
          Redirect("/forms/rates")
        }
      }
    )
  }

  def updateRate(id: Int): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val rate = rateRepo.getByIdOption(id)
    rate.map(rate => {
      val prodForm = updateRateForm.fill(UpdateRateForm(rate.get.id, rate.get.amount, rate.get.description,rate.get.username_id))
      Ok(views.html.update_rate(prodForm))
    })
  }

  def updateRateHandle(): Action[AnyContent] = Action.async { implicit request =>
    updateRateForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.update_rate(errorForm))
        )
      },
      rate => {
        rateRepo.update(rate.id, Rate(rate.id, rate.amount, rate.description,rate.username_id)).map { _ =>
          Redirect("/forms/rates")
        }
      }
    )
  }

  def deleteRate(id: Int): Action[AnyContent] = Action {
    rateRepo.delete(id)
    Redirect("/forms/rates")
  }

  // utilities

  val rateForm: Form[CreateRateForm] = Form {
    mapping(
      "amount" -> number,
      "description" -> nonEmptyText,
      "username_id" -> number,
    )(CreateRateForm.apply)(CreateRateForm.unapply)
  }

  val updateRateForm: Form[UpdateRateForm] = Form {
    mapping(
      "id" -> number,
      "amount" -> number,
      "description" -> nonEmptyText,
      "username_id" -> number,
    )(UpdateRateForm.apply)(UpdateRateForm.unapply)
  }


}

case class CreateRateForm(amount: Int, description: String, username_id: Int)
case class UpdateRateForm(id: Int, amount: Int, description: String, username_id: Int)
