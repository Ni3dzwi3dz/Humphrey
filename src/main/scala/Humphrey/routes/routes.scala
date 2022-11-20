package Humphrey.routes

import Humphrey.controllers.orderController.{OrderRep, processOrder}
import Humphrey.controllers.screeningsController._
import Humphrey.controllers.orderController.JsonFormatter._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

object Routes extends Directives with SprayJsonSupport {

  lazy val route =
    pathPrefix("screenings") {
      path(Segment){  // for info about a single screening
        id => get { complete(getSingleScreening(id.toInt))}
      } ~
      parameters("startDate","endDate") {(startDate,endDate) => //for info about screening between two dates
          complete(getScreeningsBetweenDates(startDate,endDate))
        }~
      get {    // for info about all screenings
          complete(getAllScreenings)
        }
    }~
    pathPrefix("orders"){
      post{  // for posting new order
        entity(as[OrderRep]){ order =>
          processOrder(order)
          complete(StatusCodes.OK)}
      }~
      get{
        complete(OrderRep(1,"Grzegorz","Brzęczyszczykiewicz",List((1,1,'n'),(1,2,'n'))))
      }
    }~
    complete(StatusCodes.BadRequest)

}
