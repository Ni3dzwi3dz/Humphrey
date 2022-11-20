package Humphrey.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import Humphrey.controllers.screeningsController._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives


object screeningRoutes extends Directives with SprayJsonSupport {
  // TODO: add validation, esp. for screening dates
  lazy val getAllRoute =
    pathPrefix("screenings") {
      path(Segment){
        id => get { complete(getSingleScreening(id.toInt))}
      } ~
      get {
           complete(getAllScreenings)
            }
    }~
      complete(StatusCodes.BadRequest)


}