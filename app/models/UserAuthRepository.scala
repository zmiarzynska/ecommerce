package models

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class UserAuthRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends IdentityService[UserAuth]  {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig._
  import profile.api._

  case class UserAuthDto(id: Int, providerId: String, providerKey: String, email: String)


  class UserAuthTable(tag: Tag) extends Table[UserAuthDto](tag, "userAuth") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def providerId = column[String]("providerId")

    def providerKey = column[String]("providerKey")

    def email = column[String]("email")


    def * = (id, providerId, providerKey, email) <> ((UserAuthDto.apply _).tupled, UserAuthDto.unapply)
  }


  /**
   * The starting point for all queries on the people table.
   */


   val user = TableQuery[UserAuthTable]

  override def retrieve(loginInfo: LoginInfo): Future[Option[UserAuth]] = db.run {
    user.filter(_.providerId === loginInfo.providerID)
      .filter(_.providerKey === loginInfo.providerKey)
      .result
      .headOption
  }.map(_.map(dto => toModel(dto)))


  def create(providerId: String, providerKey: String, email: String): Future[UserAuth] = db.run {
    (user.map(c => (c.providerId, c.providerKey, c.email))
      returning user.map(_.id)
      into { case ((providerId, providerKey, email), id) => UserAuthDto(id, providerId, providerKey, email) }
      ) += (providerId, providerKey, email)
  }.map(dto => toModel(dto))

  /**
   * List all the people in the database.
   */
  def list(): Future[Seq[UserAuth]] = db.run {
    user.result
  }.map(_.map(dto => toModel(dto)))


  def getById(id: Int): Future[UserAuth] = db.run {
    user.filter(_.id === id).result.head
  }.map(dto => toModel(dto))

  def getByIdOption(id: Int): Future[Option[UserAuth]] = db.run {
    user.filter(_.id === id).result.headOption
  }.map(_.map(dto => toModel(dto)))

  def delete(id: Int): Future[Int] = db.run(user.filter(_.id === id).delete)

  def update(id: Int, newUser: UserAuth): Future[UserAuth] = {
    val userToUpdate = newUser.copy(id)
    db.run {
      user.filter(_.id === id)
        .update(toDto(userToUpdate))
        .map(_ => userToUpdate)
    }
  }

  private def toModel(dto: UserAuthDto): UserAuth =
    UserAuth(dto.id, LoginInfo(dto.providerId, dto.providerKey), dto.email)

  private def toDto(model: UserAuth): UserAuthDto =
    UserAuthDto(model.id, model.loginInfo.providerID, model.loginInfo.providerKey, model.email)
}