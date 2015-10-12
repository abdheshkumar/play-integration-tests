package controllers

import dal.LibraryRepository
import model.Customer
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json._
import play.api.mvc.{Action, Controller}

/**
 * Created by abdhesh on 10/10/15.
 */
object Customers extends Controller {
  implicit val customerFormat = Json.format[Customer]

  val addCustomerForm = Form(tuple(
    "name" -> text,
    "address" -> text
  )
  )

  def getCustomer(id: Int) = Action { request =>
    val customerOpt = LibraryRepository.getCustomer(id)
    if (customerOpt.isDefined) {
      Ok(Json.prettyPrint(Json.obj(
        "status" -> "200",
        "customer" -> customerOpt.get
      )))
    } else {
      BadRequest(Json.prettyPrint(Json.obj(
        "status" -> "400",
        "message" -> s"Customer not found with id $id.")))
    }
  }

  def addCustomer = Action { implicit request =>
    val (name, address) = addCustomerForm.bindFromRequest().get
    val customer = LibraryRepository.addCustomer(name, address)
    Status(201)(Json.prettyPrint(Json.obj(
      "status" -> "201",
      "customer" -> customer,
      "message" -> "New customer created!"
    )))
  }
}
