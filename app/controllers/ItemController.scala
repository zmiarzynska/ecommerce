package controllers

import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText, number}

import javax.inject._
import play.api.mvc._
import play.api.libs.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future



@Singleton
class ItemController @Inject() (cc: ControllerComponents
                                //, item: Item
                               ) extends AbstractController(cc) {

case class CreateItemForm(name: String, description: String,  category: Int)

  val itemForm: Form[CreateItemForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "category" -> number,
    )(CreateItemForm.apply)(CreateItemForm.unapply)
  }


  def createItem():Action[AnyContent] = Action {
    NoContent
  }


  def readItem(id: Int) ={
    Action.async { implicit request =>
      Future {
        Ok("")
      }
    }
  }

  def readAllItems() ={
    Action.async { implicit request =>
      Future {
        Ok("")
      }
    }
  }

  def updateItem(id: Int): Action[AnyContent] = {Action.async { implicit request =>
    Future {
      Ok("")
    }
  }
  }

  def deleteItem(id: Int): Action[AnyContent] = Action {
    NoContent
  }

}