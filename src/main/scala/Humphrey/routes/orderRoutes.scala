package Humphrey.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives

import Humphrey.controllers.orderController._
import Humphrey.controllers.orderController.JsonFormatter._

object orderRoutes extends Directives{

  lazy val orderRoute =
    pathPrefix("orders"){
      post{
        entity(as[OrderRep]){ order =>
          //insertOrderToBase(order)
          complete(StatusCodes.OK)}
      }
    }~
      complete(StatusCodes.BadRequest)



}
