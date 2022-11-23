package Humphrey.controllers

import java.sql.Timestamp
import scala.concurrent.Await
import scala.concurrent.duration._
import slick.jdbc.SQLiteProfile.api._

import Humphrey.Humphrey.db
import Humphrey.controllers.screeningsController.{reservationsForScreeningQuery, screeningRep, singleScreening}

object ControlUtils {

  def resultToScreeningRep(i: (Int,Timestamp, String, Int)): screeningRep  = i match {
    case (id,date,title,room) => screeningRep(id,date.toString,title,room)
  }

  def resultToSingleScreening(screeningInfo: (Int,String,String,Timestamp,Int,Int)): singleScreening = screeningInfo match {
    case (id,title,director,timestamp: Timestamp,rows,seats) => singleScreening(id,title,director,timestamp.toString,createSeatsMap(rows,seats,id))
  }

  def checkIfIsBetweenDates(screen: screeningRep,startDate: String, endDate: String): Boolean =
    Timestamp.valueOf(screen.date).compareTo(stringStamp(startDate)) >= 0 &&
      Timestamp.valueOf(screen.date).compareTo(stringStamp(endDate)) <= 0

  def stringStamp(dateString: String): Timestamp = {
    val chop = dateString.grouped(2).toList
    Timestamp.valueOf(s"${chop.head}${chop(1)}-${chop(2)}-${chop(3)} ${chop(4)}:${chop(5)}:${chop(6)}")
  }

  def createSeatsMap(rows: Int, seats: Int, screeningId: Int): String = {

    val seatsArray = createSeatsArray(rows, seats, screeningId)

    seatsArray.map(_.mkString("-")).zipWithIndex.map(i => "Row" + i._2.toString + ": " + i._1 + "\n").mkString("")
  }

  def createSeatsArray(rows: Int, seats: Int, screeningId: Int): Array[Array[Any]] = {
    val reservedSeats = Await.result(db.run(reservationsForScreeningQuery(2).result),2.seconds).toList

    def filler(i: Int,j: Int): Any = if (reservedSeats.contains((i,j))) 'X' else j
    Array.tabulate(rows,seats)(filler)
  }
}
