package Humphrey.controllers

import Humphrey.Humphrey.db
import Humphrey.models.Models._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import slick.jdbc.SQLiteProfile.api._
import spray.json._

import scala.concurrent.Await
import scala.concurrent.duration._


object orderController {

  case class OrderRep(screeningId: Int, name: String, surname: String, seats: List[(Int, Int, Char)])

  object JsonFormatter extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val orderFormat: RootJsonFormat[OrderRep] = jsonFormat4(OrderRep)
  }

  import JsonFormatter._

  def processOrder(ord: OrderRep) = {

   insertOrderToBase(ord)

  }

  def validateOrder(ord: OrderRep) = {
    //check time
    // check name and surname
    //check places
  }

  def insertOrderToBase(ord: OrderRep) = {
    val orderInsert = (orders returning orders.map(_.orderId)) += (0,ord.screeningId,ord.name,ord.surname)
    val newOrderId = (Await.result(db.run(orderInsert),2.seconds))

    val reservationsInsert = reservations ++= ord.seats.map(i => (0,newOrderId,i._1,i._2,i._3))
    val resInsert = Await.result(db.run(reservationsInsert),2.seconds)


  }
}