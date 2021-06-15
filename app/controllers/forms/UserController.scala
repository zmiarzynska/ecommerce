package controllers.forms

import models.User
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import models.UserRepository

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class UserController @Inject()(userRepo: UserRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {
val userUrl = "/forms/users"
  def listUsers: Action[AnyContent] = Action.async { implicit request =>
    userRepo.list().map(users => Ok(views.html.list_users(users)))
  }

  def createUser(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val users = userRepo.list()
    users.map(_ => Ok(views.html.create_user(userForm)))
  }

  def createUserHandle(): Action[AnyContent] = Action.async { implicit request =>
    userForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.create_user(errorForm))
        )
      },
      user => {
        userRepo.create(user.username, user.password).map { _ =>
          Redirect(userUrl)
        }
      }
    )
  }

  def updateUser(id: Int): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val user = userRepo.getByIdOption(id)
    user.map(user => {
      val prodForm = updateUserForm.fill(UpdateUserForm(user.get.id, user.get.username, user.get.password))
      Ok(views.html.update_user(prodForm))
    })
  }

  def updateUserHandle(): Action[AnyContent] = Action.async { implicit request =>
    updateUserForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.update_user(errorForm))
        )
      },
      user => {
        userRepo.update(user.id, User(user.id, user.username, user.password)).map { _ =>
          Redirect(userUrl)
        }
      }
    )
  }

  def deleteUser(id: Int): Action[AnyContent] = Action {
    userRepo.delete(id)
    Redirect(userUrl)
  }


  val userForm: Form[CreateUserForm] = Form {
    mapping(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText,
    )(CreateUserForm.apply)(CreateUserForm.unapply)
  }

  val updateUserForm: Form[UpdateUserForm] = Form {
    mapping(
      "id" -> number,
      "username" -> nonEmptyText,
      "password" -> nonEmptyText,
    )(UpdateUserForm.apply)(UpdateUserForm.unapply)
  }
}

case class CreateUserForm(username: String, password: String)
case class UpdateUserForm(id: Int, username: String, password: String)