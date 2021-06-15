package controllers.forms

import models.Account
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import models.AccountRepository

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class AccountController @Inject()(accountRepo: AccountRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {
val accountUrl = "/forms/accounts"
  def listAccounts: Action[AnyContent] = Action.async { implicit request =>
    accountRepo.list().map(accounts => Ok(views.html.list_accounts(accounts)))
  }

  def createAccount(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val accounts = accountRepo.list()
    accounts.map(_ => Ok(views.html.create_account(accountForm)))
  }

  def createAccountHandle(): Action[AnyContent] = Action.async { implicit request =>
    accountForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.create_account(errorForm))
        )
      },
      account => {
        accountRepo.create(account.first_name,account.last_name,account.city).map { _ =>
          Redirect("/forms/accounts")
        }
      }
    )
  }

  def updateAccount(id: Int): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val account = accountRepo.getByIdOption(id)
    account.map(acc => {
      val prodForm = updateAccountForm.fill(UpdateAccountForm(acc.get.id, acc.get.first_name, acc.get.last_name,acc.get.city))
      Ok(views.html.update_account(prodForm))
    })
  }

  def updateAccountHandle(): Action[AnyContent] = Action.async { implicit request =>
    updateAccountForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.update_account(errorForm))
        )
      },
      account => {
        accountRepo.update(account.id, Account(account.id, account.first_name,account.last_name,account.city)).map { _ =>
          Redirect(accountUrl)
        }
      }
    )
  }

  def deleteAccount(id: Int): Action[AnyContent] = Action {
    accountRepo.delete(id)
    Redirect(accountUrl)
  }


  val accountForm: Form[CreateAccountForm] = Form {
    mapping(
      "first_name" -> nonEmptyText,
      "last_name" -> nonEmptyText,
      "city" -> nonEmptyText
    )(CreateAccountForm.apply)(CreateAccountForm.unapply)
  }

  val updateAccountForm: Form[UpdateAccountForm] = Form {
    mapping(
      "id" -> number,
      "first_name" -> nonEmptyText,
      "last_name" -> nonEmptyText,
      "city" -> nonEmptyText
    )(UpdateAccountForm.apply)(UpdateAccountForm.unapply)
  }
}

case class CreateAccountForm(first_name: String, last_name: String, city: String)
case class UpdateAccountForm(id: Int, first_name: String, last_name: String, city: String)