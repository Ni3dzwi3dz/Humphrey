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
        println(screeningsBetweenDates("2022-01-01 15:00:00","2022-11-15 22:00:00"))
        println(getAllScreenings)
        complete(getAllScreenings)
      }
    }
  }
}