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

                               ) extends AbstractController(cc) {

  def createCategory(): Action[JsValue] = Action.async(parse.json){
    implicit request =>


    request.body.validate[Category].map {
      category =>
        categoryRepo.create(category.name).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("incorrect data have been provided")))

}



  def readCategory(id: Int) = Action.async {


    val category = categoryRepo.read(id)
    category.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("category with given id not found")
    }
  }


  def readAllCategories() = Action.async {

    val categories = categoryRepo.list()
    categories.map { categories =>
      Ok(Json.toJson(categories))
    }
  }


  def updateCategory(id: Int): Action[JsValue] = Action.async(parse.json) { request =>

    request.body.validate[Category].map {
      category =>
        categoryRepo.update(id, category).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("invalid json")))


  }


  def deleteCategory(id: Int): Action[AnyContent] = Action.async {

    categoryRepo.delete(id).map { res =>
      Ok(Json.toJson(res))
    }
  }

}

