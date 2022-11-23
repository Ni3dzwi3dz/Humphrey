package Humphrey.controllers

import scala.concurrent.Await
import scala.concurrent.duration._
import slick.jdbc.SQLiteProfile.api._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._
import Humphrey.Humphrey._
import Humphrey.models.Models.{movies, orders, reservations, rooms, screenings}
import ControlUtils._

import java.sql.Timestamp


object screeningsController {

  /* This part is responsible for marshalling screening data to json format.
Case classes are used as data templates for Spray formatter
 */

  case class screeningRep(id: Int, date: String, title: String, room: Int)
  case class screeningsList(l: List[screeningRep])
  case class singleScreening(id: Int, title: String, director: String, date: String, avaiableSeats: String)

  object JsonFormatter extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val screeningFormat: RootJsonFormat[screeningRep] = jsonFormat4(screeningRep)
    implicit val screeningListFormat: RootJsonFormat[screeningsList] = jsonFormat1(screeningsList)
    implicit val singleScreeningFormat: RootJsonFormat[singleScreening] = jsonFormat5(singleScreening)
  }

  import JsonFormatter._

  // Database queries

  val allScreeningsQuery = for {
    (s, m) <- screenings join movies on (_.movieId === _.movieId)
  } yield (s.screeningId,s.screeningTime, m.title, s.roomId)

  val singleScreeningQuery: Int => Query[(Rep[Int], Rep[String], Rep[String], Rep[Timestamp], Rep[Int], Rep[Int]), (Int, String, String, Timestamp, Int, Int), Seq] =
    i => for {
    s <- screenings if s.screeningId === i
    m <- movies if m.movieId === s.movieId
    r <- rooms if r.roomId === s.roomId
  } yield (s.screeningId,m.title,m.director,s.screeningTime, r.rows, r.seats)

  val reservationsForScreeningQuery: Int => Query[(Rep[Int], Rep[Int]), (Int, Int), Seq] = id => for{
    (r,o) <- reservations join orders on (_.orderId === _.orderId) if o.screeningId === id
  } yield (r.row,r.seat)

  // Controller functions


  /*
  Basic function, that retrieves all screenings from the database and returns them as JSON.
  For clarity, mapping the results to ScreeningRep instance was moved to resultToScreeningRep in controlUtils
   */
  val getAllScreenings: JsValue = screeningsList(Await.result(db.run(allScreeningsQuery.result), 1.seconds).
    map(i => resultToScreeningRep(i)).toList.sortBy(i => (i.title,i.date))).toJson

  /*
  This function is used to filter the screenings, to show only the ones between chosen dates. It also uses allScreeningsQuery, and then filters it
  using strings with start and end date.
  Strings should be passed in "YYYYMMDDhhmmss" format - for the sake of requests simplicity, they will be converted before
  comparing in ControlUtils.checkIfIsBetweenDates method

   */
  val getScreeningsBetweenDates: (String, String) => JsValue = (startDate: String, endDate: String) =>
    screeningsList(Await.result(db.run(allScreeningsQuery.result), 2.seconds).map(i => resultToScreeningRep(i)).toList.
      filter(i => checkIfIsBetweenDates(i, startDate, endDate))).toJson


  val getSingleScreening: Int => Either[String,singleScreening] =  id => {
    val result = Await.result(db.run(singleScreeningQuery(id).take(1).result.headOption), 2.seconds)

    result match {
      case Some((id, title, director, timestamp: Timestamp, rows, seats)) => Right(resultToSingleScreening(result.get))
      case _ => Left("Nothing found. Check if there is a screening with desired Id")
    }
  }
}
