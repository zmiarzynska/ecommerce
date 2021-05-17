package controllers

import models.{Account, AccountRepository}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class AccountController @Inject()(cc: ControllerComponents,
                              accountRepository: AccountRepository
                               ) (implicit ec: ExecutionContext) extends AbstractController(cc) {



  def createAccount(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[Account].map {
      account =>
        accountRepository.create(account.first_name, account.last_name, account.city).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("")))
  }

  def readAccount(id: Int): Action[AnyContent] = Action async {
    val account = accountRepository.getByIdOption(id)
    account.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("")
    }
  }

  def updateAccount(id: Int):  Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Account].map {
      account =>
        accountRepository.update(account.id, account).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("")))
  }

  def deleteAccount(id: Int): Action[AnyContent] = Action.async {
    accountRepository.delete(id).map { res =>
      Ok(Json.toJson(res))
    }
  }

}