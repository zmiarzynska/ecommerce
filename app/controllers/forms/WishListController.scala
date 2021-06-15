package controllers.forms

import models.{Category, WishList}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import models.{CategoryRepository, WishListRepository}

import javax.inject._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class WishListController @Inject()(wishListRepo: WishListRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

 val wishUrl = "/forms/wishLists"
  def listWishLists(): Action[AnyContent] = Action.async { implicit request =>
    wishListRepo.list().map(wishLists => Ok(views.html.list_wishLists(wishLists)))
  }

  def createWishList(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>

    val wishLists = wishListRepo.list()
    wishLists.map(_ => Ok(views.html.create_wishList(wishListForm)))
  }

  def createWishListHandle(): Action[AnyContent] = Action.async { implicit request =>
    wishListForm.bindFromRequest.fold(
      errorForm => {
        println("bad request")
        Future.successful(
          BadRequest(views.html.create_wishList(errorForm))
        )
      },
      wishList => {
        wishListRepo.create(wishList.usernameId,wishList.itemId).map { _ =>
          Redirect(wishUrl)
        }
      }
    )
  }

  def updateWishList(id: Int): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val wishList = wishListRepo.getByIdOption(id)
    wishList.map(wishList => {
      val prodForm = updateWishListForm.fill(UpdateWishListForm(wishList.get.id, wishList.get.usernameId,wishList.get.itemId))
      Ok(views.html.update_wishList(prodForm))
    })
  }

  def updateWishListHandle(): Action[AnyContent] = Action.async { implicit request =>
    updateWishListForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.update_wishList(errorForm))
        )
      },
      wishList => {
        wishListRepo.update(wishList.id, WishList(wishList.id,wishList.usernameId,wishList.itemId)).map { _ =>
          Redirect(wishUrl)
        }
      }
    )
  }

  def deleteWishList(id: Int): Action[AnyContent] = Action {
    wishListRepo.delete(id)
    Redirect(wishUrl)
  }

  // utilities

  val wishListForm: Form[CreateWishListForm] = Form {
    mapping(
      "usernameId" -> number,
      "itemId" -> number
    )(CreateWishListForm.apply)(CreateWishListForm.unapply)
  }

  val updateWishListForm: Form[UpdateWishListForm] = Form {
    mapping(
      "id" -> number,
      "usernameId" -> number,
      "itemId" -> number
    )(UpdateWishListForm.apply)(UpdateWishListForm.unapply)
  }


}

case class CreateWishListForm(usernameId: Int, itemId: Int)
case class UpdateWishListForm(id: Int, usernameId: Int, itemId: Int)
