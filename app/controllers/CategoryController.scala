package controllers

import models.Category
import models.CategoryRepository
import play.api.Logger
import play.api.libs.json.{Json,JsValue}
import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future,ExecutionContext}


@Singleton
class CategoryController @Inject()(val categoryRepo: CategoryRepository, cc: ControllerComponents
                                   //, category: Category
                               ) extends AbstractController(cc) {


  val logger: Logger = Logger(classOf[CategoryController])

  def createCategory(): Action[JsValue] = Action.async(parse.json){
    implicit request =>
    logger.info("createCategory()")
    logger.info(s"${request.body}")

    request.body.validate[Category].map {
      category =>
        categoryRepo.create(category.name).map { res =>
          logger.debug(s"category created: $res")
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("incorrect data have been provided")))

}



  def readCategory(id: Int) = Action.async {
    logger.info(s"getCategoryByName($id)")

    val category = categoryRepo.read(id)
    category.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("category with given id not found")
    }
  }


  def readAllCategories() = Action.async {
    logger.info(s"readAllCategories()")

    val categories = categoryRepo.list()
    categories.map { categories =>
      Ok(Json.toJson(categories))
    }
  }


  def updateCategory(id: Int): Action[JsValue] = Action.async(parse.json) { request =>
    logger.info(s"updateCategory()")
    logger.debug(s"${request.body}")

    request.body.validate[Category].map {
      category =>
        categoryRepo.update(id, category).map { res =>
          logger.debug(s"categories updated count: $res")
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("invalid json")))


  }


  def deleteCategory(id: Int): Action[AnyContent] = Action.async {
    logger.info(s"deleteCategory($id)")

    categoryRepo.delete(id).map { res =>
      logger.debug(s"categories deleted count: $res")
      Ok(Json.toJson(res))
    }
  }

}



case class CreateCategoryForm(name: String)
case class UpdateCategoryForm(id: Int, name: String)