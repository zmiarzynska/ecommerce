package controllers.forms

import models.{Category, Item}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import models.{CategoryRepository, ItemRepository}

import javax.inject._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class ItemController @Inject()(itemRepo: ItemRepository, categoryRepo: CategoryRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  var categoryList: Seq[Category] = Seq[Category]()

  // fill above lists
  fetchData()

  def listItems: Action[AnyContent] = Action.async { implicit request =>
    itemRepo.list().map(items => Ok(views.html.list_items(items)))
  }

  def createItem(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val categories = categoryRepo.list()
    categories.map(categories => Ok(views.html.create_item(itemForm, categories)))
  }

  def createItemHandle(): Action[AnyContent] = Action.async { implicit request =>
    itemForm.bindFromRequest.fold(
      errorForm => {
        println("bad request - create item handle in ItemController.scala")
        Future.successful(
          BadRequest(views.html.create_item(errorForm, categoryList))
        )
      },
      item => {
        itemRepo.create(item.name, item.description, item.price, item.category).map { _ =>
          Redirect("/forms/items")
        }
      }
    )
  }

  def updateItem(id: Int): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val item = itemRepo.getByIdOption(id)
    item.map(item => {
      val prodForm = updateItemForm.fill(UpdateItemForm(item.get.id, item.get.category, item.get.name, item.get.price, item.get.description))
      Ok(views.html.update_item(prodForm, categoryList))
    })
  }

  def updateItemHandle(): Action[AnyContent] = Action.async { implicit request =>
    updateItemForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.update_item(errorForm, categoryList))
        )
      },
      item => {
        itemRepo.update(item.id, Item(item.id, item.name, item.price, item.description, item.category)).map { _ =>
          Redirect("/forms/items")
        }
      }
    )
  }

  def deleteItem(id: Int): Action[AnyContent] = Action {
    itemRepo.delete(id)
    Redirect("/forms/items")
  }

  // utilities

  val itemForm: Form[CreateItemForm] = Form {
    mapping(
      "category" -> number,
      "name" -> nonEmptyText,
      "price"-> number,
      "description" -> nonEmptyText,
    )(CreateItemForm.apply)(CreateItemForm.unapply)
  }

  val updateItemForm: Form[UpdateItemForm] = Form {
    mapping(
      "id" -> number,
      "category" -> number,
      "name" -> nonEmptyText,
      "price"-> number,
      "description" -> nonEmptyText,
    )(UpdateItemForm.apply)(UpdateItemForm.unapply)
  }

  def fetchData(): Unit = {

    categoryRepo.list().onComplete {
      case Success(category) => categoryList = category
      case Failure(e) => print("error while listing categories", e)
    }

  }
}

case class CreateItemForm(category: Int, name: String, price: Int, description: String)
case class UpdateItemForm(id: Int, category: Int, name: String, price: Int, description: String)
