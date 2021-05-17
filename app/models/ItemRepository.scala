package models

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ Future, ExecutionContext }

@Singleton
class ItemRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, categoryRepository: CategoryRepository)(implicit ec: ExecutionContext) {
   val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._


  private class ItemTable(tag: Tag) extends Table[Item](tag, "item") {

    /** The ID column, which is the primary key, and auto incremented */
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    /** The name column */
    def name = column[String]("name")

    /** The age column */
    def description = column[String]("description")

    def category = column[Int]("category")

    def category_fk = foreignKey("cat_fk",category, cat)(_.id)


    /**
     * This is the tables default "projection".
     *
     * It defines how the columns are converted to and from the Person object.
     *
     * In this case, we are simply passing the id, name and page parameters to the Person case classes
     * apply and unapply methods.
     */
    def * = (id, name, description, category) <> ((Item.apply _).tupled, Item.unapply)

  }

  /**
   * The starting point for all queries on the people table.
   */

  import categoryRepository.CategoryTable

  private val item = TableQuery[ItemTable]

  private val cat = TableQuery[CategoryTable]


  /**
   * Create a person with the given name and age.
   *
   * This is an asynchronous operation, it will return a future of the created person, which can be used to obtain the
   * id for that person.
   */
  def create(name: String, description: String, category: Int): Future[Item] = db.run {
    // We create a projection of just the name and age columns, since we're not inserting a value for the id column
    (item.map(p => (p.name, p.description,p.category))
      // Now define it to return the id, because we want to know what id was generated for the person
      returning item.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into {case ((name,description,category),id) => Item(id,name, description,category)}
      // And finally, insert the item into the database
      ) += (name, description,category)
  }

  /**
   * List all the people in the database.
   */
  def list(): Future[Seq[Item]] = db.run {
    item.result
  }

  def getByCategory(category_id: Int): Future[Seq[Item]] = db.run {
    item.filter(_.category === category_id).result
  }

  def getById(id: Int): Future[Item] = db.run {
    item.filter(_.id === id).result.head
  }

  def getByIdOption(id: Int): Future[Option[Item]] = db.run {
    item.filter(_.id === id).result.headOption
  }

  def getByCategories(category_ids: List[Int]): Future[Seq[Item]] = db.run {
    item.filter(_.category inSet category_ids).result
  }

  def delete(id: Int): Future[Int] = db.run(item.filter(_.id === id).delete)

  def update(id: Int, new_item: Item): Future[Int] = {
    val itemToUpdate: Item = new_item.copy(id)
    db.run(item.filter(_.id === id).update(itemToUpdate))
  }

}