package Humphrey.controllers

import scala.concurrent.Await
import scala.concurrent.duration._
import slick.jdbc.SQLiteProfile.api._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._
import Humphrey.Humphrey._
import Humphrey.models.Models.{movies, orders, rooms, screenings}
import ControlUtils._

import java.sql.Timestamp


object screeningsController {

  /* This part is responsible for marshalling screening data to json format.
Case classes are used as data templates for Spray formatter
 */

  case class screeningRep(date: String, title: String, room: Int)
  case class screeningsList(l : List[screeningRep])

  object JsonFormatter extends SprayJsonSupport with DefaultJsonProtocol{
    implicit val screeningFormat: RootJsonFormat[screeningRep] = jsonFormat3(screeningRep)
    implicit val screeningListFormat: RootJsonFormat[screeningsList] = jsonFormat1(screeningsList)
  }
  import JsonFormatter._


  val allScreeningsQuery = for {
    (s,m) <- screenings join movies on (_.movieId === _.movieId)
  } yield (s.screeningTime, m.title, s.roomId)

  val singleScreeningForIdQuery: Int => Query[(Rep[Timestamp], Rep[String], Rep[Int]), (Timestamp, String, Int), Seq] = i => for {
    (s,m) <- screenings join movies on (_.movieId ===_.movieId) if m.movieId === i
  } yield (s.screeningTime, m.title, s.roomId)

  val screeningsBetweenDates : (String,String) => Query[(Rep[Timestamp], Rep[String], Rep[Int]), (Timestamp, String, Int), Seq] =
    (start, end) => for {
      (s,m) <- screenings join movies on (_.movieId ===_.movieId)
        if s.screeningTime.compareTo(Timestamp.valueOf(start)) >= 0 && s.screeningTime.compareTo(Timestamp.valueOf(end)) < 0
    } yield (s.screeningTime, m.title, s.roomId)

  val getAllScreenings: JsValue = screeningsList(Await.result(db.run(allScreeningsQuery.result),1.seconds).map(i =>resultToScreeningRep(i)).toList).toJson

  val getScreeningsBetweenDates: (String, String) => JsValue = (startDate: String, endDate: String) =>
    screeningsList(Await.result(db.run(allScreeningsQuery.result),2.seconds).map(i =>resultToScreeningRep(i)).toList.
      filter(i => true)
      )).toJson








}

