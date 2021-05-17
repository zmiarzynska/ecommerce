package controllers

import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


@Singleton
class WishListController @Inject()(cc: ControllerComponents
                                   //, wishList: WishList
                              ) extends AbstractController(cc) {



  def createWishList():Action[AnyContent] = Action {
    NoContent
  }


  def readWishList(id: Int) ={
    Action.async { implicit request =>
      Future {
        Ok("")
      }
    }
  }

  def updateWishList(id: Int): Action[AnyContent] = {Action.async { implicit request =>
    Future {
      Ok("")
    }
  }
  }

  def deleteWishList(id: Int): Action[AnyContent] = Action {
    NoContent
  }

}