package controllers

import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


@Singleton
class RateController @Inject()(cc: ControllerComponents
                               //, rate: Rate
                              ) extends AbstractController(cc) {



  def createRate():Action[AnyContent] = Action {
    NoContent
  }


  def readRate(id: Int) ={
    Action.async { implicit request =>
      Future {
        Ok("")
      }
    }
  }

  def readAllRates() ={
    Action.async { implicit request =>
      Future {
        Ok("")
      }
    }
  }

  def updateRate(id: Int): Action[AnyContent] = {Action.async { implicit request =>
    Future {
      Ok("")
    }
  }
  }

  def deleteRate(id: Int): Action[AnyContent] = Action {
    NoContent
  }

}