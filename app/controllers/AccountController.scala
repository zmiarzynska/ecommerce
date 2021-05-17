package controllers

import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


@Singleton
class AccountController @Inject()(cc: ControllerComponents
                                  //, account: Account
                               ) extends AbstractController(cc) {



  def createAccount():Action[AnyContent] = Action {
    NoContent
  }


  def readAccount(id: Int) ={
    Action.async { implicit request =>
      Future {
        Ok("")
      }
    }
  }

  def updateAccount(id: Int): Action[AnyContent] = {Action.async { implicit request =>
    Future {
      Ok("")
    }
  }
  }

  def deleteAccount(id: Int): Action[AnyContent] = Action {
    NoContent
  }

}