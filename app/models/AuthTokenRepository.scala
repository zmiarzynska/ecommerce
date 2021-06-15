package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps

@Singleton
class AuthTokenRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class AuthTokenTable(tag: Tag) extends Table[AuthToken](tag, "authToken") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def userId = column[Int]("userId")

    def * = (id, userId) <> ((AuthToken.apply _).tupled, AuthToken.unapply)
  }

  val authToken = TableQuery[AuthTokenTable]

  def create(userId: Int): Future[AuthToken] = db.run {
    (authToken.map(r => r.userId)
      returning authToken.map(_.id)
      into { case (userId, id) => AuthToken(id, userId) }
      ) += userId
  }
}