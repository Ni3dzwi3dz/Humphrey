package com.example

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import Humphrey.routes.screeningRoutes.getAllRoute
import Humphrey.controllers.screeningsController._
import Humphrey.controllers.screeningsController.JsonFormatter._

class routesTests extends AnyWordSpec with Matchers with ScalatestRouteTest {

"The service" should {
  "return all screenings, when using the root path" in {
    Get("/screenings") ~> getAllRoute ~> check {
      status shouldEqual StatusCodes.OK

    }
  }
}

}
