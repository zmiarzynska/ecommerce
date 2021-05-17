package controllers

import models.{Rate, RateRepository}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


@Singleton
class RateController @Inject()(cc: ControllerComponents,
                               rateRepository: RateRepository,
                              ) extends AbstractController(cc) {

  def createRate(): Action[JsValue] = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Rate].map {
        rate =>
          rateRepository.create(rate.amount, rate.description, rate.username_id).map { res =>
            Ok(Json.toJson(res))
          }
      }.getOrElse(Future.successful(BadRequest("")))
  }


  def readAllRates(): Action[AnyContent] = Action.async {
    val rates = rateRepository.list()
    rates.map {
      rates => Ok(Json.toJson(rates))
    }
  }

  def readRate(id: Int): Action[AnyContent] = Action.async {
    val rate = rateRepository.getByIdOption(id)
    rate.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("")
    }
  }

  def updateRate(id: Int): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Rate].map {
      rate =>
        rateRepository.update(rate.id, rate).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("")))
  }

  def deleteRate(id: Int): Action[AnyContent] = Action.async {
    rateRepository.delete(id).map { res =>
      Ok(Json.toJson(res))
    }
  }


}