package Humphrey

import akka.actor.typed.ActorSystem
import akka.actor.typed.javadsl.Behaviors
import akka.http.scaladsl.Http
import slick.jdbc.SQLiteProfile.api._
import com.typesafe.config.ConfigFactory
import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.concurrent.duration._

import models.Models._
import TestData._
import routes.Routes.route



object Humphrey extends App {

  // Creating actor system

  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "Humphrey")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext

  // DB initialization

  val db = Database.forConfig("db")

  val setup = DBIO.seq((movies.schema ++ rooms.schema ++ screenings.schema ++ orders.schema ++ reservations.schema ++ ticketRates.schema).create,
    movies ++= moviesInsert,
    rooms ++= roomsInsert,
    screenings ++= screeningsInsert,
    orders ++= ordersInsert,
    reservations ++= reservationsInsert,
    ticketRates ++= ticketRatesInsert
  )

  val setupFuture = Await.result(db.run(setup), 30.second)

  val numberOfScreenings = sql"SELECT COUNT(screening_id) FROM screenings".as[Int]

  //creating server
  val config = ConfigFactory.load()


  Http().newServerAt(config.getString("http.host"), config.getInt("http.port")).bindFlow(route)
}