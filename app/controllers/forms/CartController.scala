package controllers.forms


import models.Cart
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import models.CartRepository

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class CartController @Inject()(cartRepo: CartRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def listCarts: Action[AnyContent] = Action.async { implicit request =>
    cartRepo.list().map(carts => Ok(views.html.list_carts(carts)))
  }

  def createCart(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val carts = cartRepo.list()
    carts.map(_ => Ok(views.html.create_cart(cartForm)))
  }

  def createCartHandle(): Action[AnyContent] = Action.async { implicit request =>
    cartForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.create_cart(errorForm))
        )
      },
      cart => {
        cartRepo.create(cart.number).map { _ =>
          Redirect("/forms/carts")
        }
      }
    )
  }

  def updateCart(id: Int): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val cart = cartRepo.getById(id)
    cart.map(cart => {
      val prodForm = updateCartForm.fill(UpdateCartForm(cart.get.id, cart.get.number))
      Ok(views.html.update_cart(prodForm))
    })
  }

  def updateCartHandle(): Action[AnyContent] = Action.async { implicit request =>
    updateCartForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.update_cart(errorForm))
        )
      },
      cart => {
        cartRepo.update(cart.id, Cart(cart.id, cart.number)).map { _ =>
          Redirect("/forms/carts")
        }
      }
    )
  }

  def deleteCart(id: Int): Action[AnyContent] = Action {
    cartRepo.delete(id)
    Redirect("/forms/carts")
  }


  val cartForm: Form[CreateCartForm] = Form {
    mapping(
      "number" -> number,
    )(CreateCartForm.apply)(CreateCartForm.unapply)
  }

  val updateCartForm: Form[UpdateCartForm] = Form {
    mapping(
      "id" -> number,
      "number" -> number,
    )(UpdateCartForm.apply)(UpdateCartForm.unapply)
  }
}

case class CreateCartForm(number: Int)
case class UpdateCartForm(id: Int, number: Int)