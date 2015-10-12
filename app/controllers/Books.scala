package controllers

import dal.LibraryRepository
import model.Book
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

/**
 * Created by abdhesh on 10/10/15.
 */
object Books extends Controller {
  implicit val bookFormat = Json.format[Book]

  def getBooks = Action { request =>
    Ok(Json.prettyPrint(Json.obj("books" -> LibraryRepository.getBooks)))
  }

  def search(author: Option[String], title: Option[String]) = Action { request =>
    val results = (author, title) match {
      case (Some(_author), Some(_title)) => LibraryRepository.getBooksByAuthorAndTitle(_author, _title)
      case (Some(_author), None) => LibraryRepository.getBooksByAuthor(_author)
      case (None, Some(_title)) => LibraryRepository.getBooksByTitle(_title)
      case _ => List()
    }
    Ok(Json.prettyPrint(Json.obj("books" -> results)))
  }

  def checkout(id: Int) = Action { request =>
    val bookOpt = LibraryRepository.getBook(id)
    if (bookOpt.isEmpty) {
      BadRequest(Json.prettyPrint(Json.obj(
        "status" -> "400",
        "message" -> s"Book not found with id $id.")))
    } else if (!bookOpt.get.available) {
      BadRequest(Json.prettyPrint(Json.obj(
        "status" -> "400",
        "message" -> s"Book #$id is already checked out.")))
    } else {
      LibraryRepository.checkoutBook(bookOpt.get)
      Ok(Json.prettyPrint(Json.obj(
        "status" -> 200,
        "book" -> bookOpt.get,
        "message" -> "Book checked out!")))
    }
  }

  def checkin(id: Int) = Action { request =>
    val bookOpt = LibraryRepository.getBook(id)
    if (bookOpt.isEmpty) {
      BadRequest(Json.prettyPrint(Json.obj(
        "status" -> "400",
        "message" -> s"Book not found with id $id.")))
    } else if (bookOpt.get.available) {
      BadRequest(Json.prettyPrint(Json.obj(
        "status" -> "400",
        "message" -> s"Book #$id is already checked in.")))
    } else {
      LibraryRepository.checkinBook(bookOpt.get)
      Ok(Json.prettyPrint(Json.obj(
        "status" -> 200,
        "book" -> bookOpt.get,
        "message" -> "Book checked back in!")))
    }
  }
}
