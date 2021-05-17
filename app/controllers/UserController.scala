package controllers

import models.{User, UserRepository}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class UserController @Inject()(cc: ControllerComponents,
                               userRepository: UserRepository
                              )(implicit ec: ExecutionContext) extends AbstractController(cc) {


  def createUser(): Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      request.body.validate[User].map {
        user =>
          userRepository.create(user.username, user.password).map { res =>
            Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("")))
  }


  def readAllUsers(): Action[AnyContent] = Action.async {
    val users = userRepository.list()
    users.map {
      users => Ok(Json.toJson(users))
    }
  }

  def readUser(id: Int): Action[AnyContent] = Action.async {
    val user = userRepository.getByIdOption(id)
    user.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("")
    }
  }

  def updateUser(id: Int): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[User].map {
      user =>
        userRepository.update(user.id, user).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("")))
  }

  def deleteUser(id: Int): Action[AnyContent] = Action.async {
    userRepository.delete(id).map { res =>
      Ok(Json.toJson(res))
    }
  }

}