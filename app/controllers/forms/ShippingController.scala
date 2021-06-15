package controllers.forms

import models.Shipping
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import models.ShippingRepository

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class ShippingController @Inject()(shippingRepo: ShippingRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {
val shippingUrl = "/forms/shippings"
  def listShippings: Action[AnyContent] = Action.async { implicit request =>
    shippingRepo.list().map(shippings => Ok(views.html.list_shippings(shippings)))
  }

  def createShipping(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val shippings = shippingRepo.list()
    shippings.map(_ => Ok(views.html.create_shipping(shippingForm)))
  }

  def createShippingHandle(): Action[AnyContent] = Action.async { implicit request =>
    shippingForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.create_shipping(errorForm))
        )
      },
      shipping => {
        shippingRepo.create(shipping.street,shipping.city,shipping.house,shipping.phone).map { _ =>
          Redirect(shippingUrl)
        }
      }
    )
  }

  def updateShipping(id: Int): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val shipping = shippingRepo.getById(id)
    shipping.map(shipping => {
      val prodForm = updateShippingForm.fill(UpdateShippingForm(shipping.get.id, shipping.get.street, shipping.get.city,shipping.get.house,shipping.get.phone))
      Ok(views.html.update_shipping(prodForm))
    })
  }

  def updateShippingHandle(): Action[AnyContent] = Action.async { implicit request =>
    updateShippingForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.update_shipping(errorForm))
        )
      },
      shipping => {
        shippingRepo.update(shipping.id, Shipping(shipping.id, shipping.street,shipping.city,shipping.house, shipping.phone)).map { _ =>
          Redirect(shippingUrl)
        }
      }
    )
  }

  def deleteShipping(id: Int): Action[AnyContent] = Action {
    shippingRepo.delete(id)
    Redirect(shippingUrl)
  }


  val shippingForm: Form[CreateShippingForm] = Form {
    mapping(
      "street" -> nonEmptyText,
      "city" -> nonEmptyText,
      "house" -> number,
      "phone" -> number
    )(CreateShippingForm.apply)(CreateShippingForm.unapply)
  }

  val updateShippingForm: Form[UpdateShippingForm] = Form {
    mapping(
      "id" -> number,
      "street" -> nonEmptyText,
      "city" -> nonEmptyText,
      "house" -> number,
      "phone" -> number
    )(UpdateShippingForm.apply)(UpdateShippingForm.unapply)
  }
}

case class CreateShippingForm(street: String, city: String, house: Int, phone: Int)
case class UpdateShippingForm(id: Int, street: String, city: String, house: Int, phone: Int)