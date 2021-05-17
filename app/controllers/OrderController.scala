package controllers

import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


@Singleton
class OrderController @Inject()(cc: ControllerComponents
                                //, myOrder: MyOrder
                               ) extends AbstractController(cc) {



  def createMyOrders():Action[AnyContent] = Action {
    NoContent
  }


  def getMyOrders() ={
    Action.async { implicit request =>
      Future {
        Ok("")
      }
    }
  }

  def readMyOrders(id: Int) ={
    Action.async { implicit request =>
      Future {
        Ok("")
      }
    }
  }





}