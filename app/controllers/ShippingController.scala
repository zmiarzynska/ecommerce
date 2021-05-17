package controllers

import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


@Singleton
class ShippingController @Inject()(cc: ControllerComponents
                                   //, shipping: Shipping
                               ) extends AbstractController(cc) {



  def createShipping():Action[AnyContent] = Action {
    NoContent
  }


  def readShipping(id: Int) ={
    Action.async { implicit request =>
      Future {
        Ok("")
      }
    }
  }

  def updateShipping(id: Int): Action[AnyContent] = {Action.async { implicit request =>
    Future {
      Ok("")
    }
  }
  }

  def deleteShipping(id: Int): Action[AnyContent] = Action {
    NoContent
  }

}