package Humphrey.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directives, Route}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

import Humphrey.controllers.orderController.{OrderRep, processOrder}
import Humphrey.controllers.screeningsController._
import Humphrey.controllers.screeningsController.JsonFormatter._
import Humphrey.controllers.orderController.JsonFormatter._
import Humphrey.controllers.orderController._

object Routes extends Directives with SprayJsonSupport {

  lazy val route: Route =
    pathPrefix("screenings") {
      path(IntNumber){                                                          // for info about a single screening
        id => get { getSingleScreening(id) match {
          case Right(singleScreening(id,title,director,date,seats)) => complete(200-> singleScreening(id,title,director,date,seats))
          case Left(msg) => complete(400 -> msg)
        }}
      } ~
      parameters("startDate","endDate") {(startDate,endDate) => //for info about screening between two dates
          complete(200 -> getScreeningsBetweenDates(startDate,endDate))
        }~
      get {                                                                   // for info about all screenings
          complete(200 -> getAllScreenings)
        }
    }~
    pathPrefix("orders"){
      post{  // for posting new order
        entity(as[OrderRep]){ order =>processOrder(order) match {
          case Success(message, orderId, expiryDate, totalCost) => complete(200 -> Success(message,orderId,expiryDate,totalCost))
          case Error(message,status) => complete(status -> message)
        }}
      }}~
    complete(StatusCodes.BadRequest)

}
