package controllers

import models.{CategoryRepository, Item, ItemRepository}
import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText, number}
import play.api.libs.json.Format.GenericFormat

import javax.inject._
import play.api.mvc._
import play.api.libs.json._

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class ItemController @Inject()(cc: ControllerComponents,
                               val itemRepository: ItemRepository,
                               val categoryRepository: CategoryRepository,
                              )(implicit ec: ExecutionContext) extends AbstractController(cc) {


  def createItem(): Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Item].map {
        item =>
          itemRepository.create(item.name, item.description, item.price, item.image,item.category).map { res =>
            Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("")))
  }

  def readItem(id: Int): Action[AnyContent] = Action.async {
    val item = itemRepository.getByIdOption(id)
    item.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("")
    }
  }

  def readAllItems(): Action[AnyContent] = Action.async {
    val items = itemRepository.list()
    items.map {
      items => Ok(Json.toJson(items))
    }
  }

  def updateItem(id: Int): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Item].map {
      item =>
        itemRepository.update(item.id, item).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("")))
  }

  def deleteItem(id: Int): Action[AnyContent] = Action.async {
    itemRepository.delete(id).map { res =>
      Ok(Json.toJson(res))
    }
  }

}