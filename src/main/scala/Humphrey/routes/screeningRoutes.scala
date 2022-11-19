package Humphrey.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import Humphrey.controllers.screeningsController._
import akka.http.scaladsl.server.Directives


object screeningRoutes extends Directives with SprayJsonSupport {

  lazy val getAllRoute =
    pathPrefix("screenings") {
      path(Segment){
        id => get { complete(getSingleScreening(id.toInt))}
      } ~
      get {
           complete(getAllScreenings)
            }
    }

  val getScreeningsBetweenDatesRoute = path("screenings"){
    parameters("startDate","endDate") {(startDate,endDate) =>
      complete(getScreeningsBetweenDates(startDate,endDate))
    }
  }



}