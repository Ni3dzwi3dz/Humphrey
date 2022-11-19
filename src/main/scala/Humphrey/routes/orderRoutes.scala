package Humphrey.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives

object orderRoutes extends Directives{

  lazy val orderRoute =
    pathPrefix("orders"){
      post{
        entity(as[String]){ id => complete(StatusCodes.OK)}
      }
    }



}
