package controllers

import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


@Singleton
class CartController @Inject()(cc: ControllerComponents
                               //, cart: Cart
                              ) extends AbstractController(cc) {


  def createCart(): Action[AnyContent] = Action {
    NoContent
  }


  def readCart(id: Int) = {
    Action.async { implicit request =>
      Future {
        Ok("")
      }
    }
  }

  def readAllCarts() ={
    Action.async { implicit request =>
      Future {
        Ok("")
      }
    }
  }

  def updateCart(id: Int): Action[AnyContent] = {
    Action.async { implicit request =>
      Future {
        Ok("")
      }
    }
  }

  def deleteCart(id: Int): Action[AnyContent] = Action {
    NoContent
  }

}