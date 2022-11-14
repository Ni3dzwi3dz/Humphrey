package com.example

import Humphrey.routes.screeningRoutes.getAllRoute
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class routesTests extends AnyWordSpec with Matchers with ScalatestRouteTest {

  "The service" should {
    "return full list of screenings, for GET requests to the /screenings path" in {
      Get("/screenings") ~> getAllRoute ~> check {
        responseAs[String] shouldEqual "{\"l\":[{\"date\":\"2022-11-15 19:15:00.0\",\"room\":1,\"title\":\"Django\"},{\"date\":\"2022-11-15 19:15:00.0\",\"room\":2,\"title\":\"Se7en\"},{\"date\":\"2022-11-15 15:15:00.0\",\"room\":1,\"title\":\"Miś\"},{\"date\":\"2022-11-16 22:15:00.0\",\"room\":3,\"title\":\"Coś\"},{\"date\":\"2022-11-15 16:15:00.0\",\"room\":3,\"title\":\"Django\"},{\"date\":\"2022-11-15 17:15:00.0\",\"room\":1,\"title\":\"Se7en\"},{\"date\":\"2022-11-15 18:15:00.0\",\"room\":2,\"title\":\"Miś\"},{\"date\":\"2022-11-16 21:15:00.0\",\"room\":1,\"title\":\"Coś\"}]}"
      }
    }
  }

}
