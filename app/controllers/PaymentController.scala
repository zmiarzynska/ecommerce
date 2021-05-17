package controllers

import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


@Singleton
class PaymentController @Inject()(cc: ControllerComponents
                                  //, payment: Payment
                               ) extends AbstractController(cc) {



  def createPayment():Action[AnyContent] = Action {
    NoContent
  }


  def readPayment(id: Int) ={
    Action.async { implicit request =>
      Future {
        Ok("")
      }
    }
  }

  def updatePayment(id: Int): Action[AnyContent] = {Action.async { implicit request =>
    Future {
      Ok("")
    }
  }
  }

  def deletePayment(id: Int): Action[AnyContent] = Action {
    NoContent
  }

}