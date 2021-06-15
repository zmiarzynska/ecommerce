package controllers.forms

import models.Category
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import models.CategoryRepository

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CategoryController @Inject()(categoryRepo: CategoryRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {
val categoryUrl = "/forms/categories"
  def readAllCategories: Action[AnyContent] = Action.async { implicit request =>
    categoryRepo.list().map(categories => Ok(views.html.list_category(categories)))
  }

  def createCategory(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val categories = categoryRepo.list()
    categories.map(_ => Ok(views.html.create_category(categoryForm)))
  }

  def createCategoryHandle(): Action[AnyContent] = Action.async { implicit request =>
    categoryForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.create_category(errorForm))
        )
      },
      category => {
        categoryRepo.create(category.name).map { _ =>
          Redirect(categoryUrl)
        }
      }
    )
  }

  def updateCategory(id: Int): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val category = categoryRepo.read(id)

    category.map(category => {
      val prodForm = updateCategoryForm.fill(UpdateCategoryForm(category.get.id, category.get.name))
      Ok(views.html.update_category(prodForm))
    })
  }

  def updateCategoryHandle(): Action[AnyContent] = Action.async { implicit request =>
    updateCategoryForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.update_category(errorForm))
        )
      },
      category => {
        categoryRepo.update(category.id, Category(category.id, category.name)).map { _ =>
          Redirect(categoryUrl)
        }
      }
    )
  }

  def deleteCategory(id: Int): Action[AnyContent] = Action {
    categoryRepo.delete(id)
    Redirect(categoryUrl)
  }


  val categoryForm: Form[CreateCategoryForm] = Form {
    mapping(
      "name" -> nonEmptyText,
    )(CreateCategoryForm.apply)(CreateCategoryForm.unapply)
  }

  val updateCategoryForm: Form[UpdateCategoryForm] = Form {
    mapping(
      "id" -> number,
      "name" -> nonEmptyText,
    )(UpdateCategoryForm.apply)(UpdateCategoryForm.unapply)
  }
}

case class CreateCategoryForm(name: String)
case class UpdateCategoryForm(id: Int, name: String)