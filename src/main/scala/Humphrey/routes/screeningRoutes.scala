package Humphrey.routes

import akka.http.scaladsl.model._
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.headers.`Content-Type`
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import Humphrey.controllers.screeningsController._
import akka.http.scaladsl.server.Directives


object screeningRoutes extends Directives with SprayJsonSupport {

  val getAllRoute = path("screenings") {
    get {
      pathEndOrSingleSlash {
        complete(getAllScreenings)
      }
    }
  }
/*
  val getScreeningsBetweenDatesRoute = path("screenings"){
    parameters("startDate","endDate") {(startDate,endDate) =>
      complete(getScreeningsBetweenDates(startDate,endDate))
    }
  }

 */
}