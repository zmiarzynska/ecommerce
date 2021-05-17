package controllers

import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


@Singleton
class UserController @Inject()(cc: ControllerComponents
                               //, user: User
                               ) extends AbstractController(cc) {



  def createUser():Action[AnyContent] = Action {
    NoContent
  }


  def readUser(id: Int) ={
    Action.async { implicit request =>
      Future {
        Ok("")
      }
    }
  }

  def readAllUsers() ={
    Action.async { implicit request =>
      Future {
        Ok("")
      }
    }
  }


  def updateUser(id: Int): Action[AnyContent] = {Action.async { implicit request =>
    Future {
      Ok("")
    }
  }
  }

  def deleteUser(id: Int): Action[AnyContent] = Action {
    NoContent
  }

}