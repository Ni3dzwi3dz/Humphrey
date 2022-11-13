package Humphrey

import akka.actor.typed.ActorSystem
import akka.actor.typed.javadsl.Behaviors
import akka.http.scaladsl.Http
import slick.jdbc.SQLiteProfile.api._
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

import java.sql.Timestamp
import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.concurrent.duration._
import models.Models._
import routes.screeningRoutes._
import controllers.screeningsController.screeningRep
import TestData._


object Humphrey extends App {

  // Creating actor system

  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "Humphrey")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext

  // DB initialization

  val db = Database.forConfig("db")

  val setup = DBIO.seq((movies.schema ++ rooms.schema ++ screenings.schema ++ orders.schema).create,
    movies ++= moviesInsert,
    rooms ++= roomsInsert,
    screenings ++= screeningsInsert
  )

  val setupFuture = Await.result(db.run(setup), 30.second)

  //creating server
  val config = ConfigFactory.load()

  val routes = getAllRoute

  Http().newServerAt(config.getString("http.host"), config.getInt("http.port")).bindFlow(routes)
}