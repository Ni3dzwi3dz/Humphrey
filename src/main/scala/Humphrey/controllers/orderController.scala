package Humphrey.controllers

import Humphrey.Humphrey.db
import Humphrey.controllers.screeningsController.singleScreeningQuery
import Humphrey.models.Models._
import ControlUtils.createSeatsArray
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import slick.jdbc.SQLiteProfile.api._
import spray.json._
import java.sql.Timestamp
import scala.concurrent.Await
import scala.concurrent.duration._


object orderController {

  case class OrderRep(screeningId: Int, name: String, surname: String, seats: List[(Int, Int, Char)])

  sealed trait Result
  case class Error(message: String, status: Int) extends Result
  case class Success(message: String, orderId: Int, expiryDate: String, totalCost: Double) extends Result

  object JsonFormatter extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val orderFormat: RootJsonFormat[OrderRep] = jsonFormat4(OrderRep)
    implicit val errorFormat: RootJsonFormat[Error] = jsonFormat2(Error)
    implicit val successFormat: RootJsonFormat[Success] = jsonFormat4(Success)
  }



  def processOrder(ord: OrderRep): Result = {

    validateOrder(ord) match {
      case Right(ord) => insertOrderToBase(ord)
      case Left(msg) => Error(msg,400)
    }
  }


  def validateOrder(ord: OrderRep): Either[String,OrderRep] = {

    val screeningInfo = Await.result(db.run(singleScreeningQuery(ord.screeningId).take(1).result.headOption), 2.seconds).get
    val seatsMap = createSeatsArray(screeningInfo._5,screeningInfo._6,ord.screeningId)


    if (!checkIfTimeIsOk(screeningInfo._4))
      Left("Problem checking time. Probably, you`re trying to book a ticket for screening in less than 15 minutes.")

    else if (!checkSeats(ord.seats,seatsMap))
      Left("There seem to be a problem with your choice of seats. Please check the booking policy, or contact support")

    else if (!(checkName(ord.name) && checkName(ord.surname)))
      Left("It seems, you entered wrong, or unusual name. If your real name or surname is less than 3 characters long, please contact support")
    else
      Right(ord)

  }

  def insertOrderToBase(ord: OrderRep): Result= {
    val orderInsert = (orders returning orders.map(_.orderId)) += (0,ord.screeningId,ord.name,ord.surname)
    val newOrderId = Await.result(db.run(orderInsert),2.seconds)

    val reservationsInsert = reservations ++= ord.seats.map(i => (0,newOrderId,i._1,i._2,i._3))
    val resInsert = Await.result(db.run(reservationsInsert),2.seconds)

    if (resInsert.get == ord.seats.length)
      Success("Your order has been recorded. Please pay in 30min. to confirm it.", newOrderId,
        new Timestamp(System.currentTimeMillis() + 180000).toString,calculateTotalAmount(newOrderId))
    else
      Error("There was a problem with saving your order. Please contact support",500)

  }
  /*
  Function checking if chosen screening will take place in more than 15 minutes.
  It takes timestamp value of screeningTime.
  This value is compared to current time + 15 minutes (15*60*1000 is 900 000 ms) - if screeningTime is after the threshold
  compareTo function should return 1

  NOTE: 15min threshold is hardcoded here. It may be better, make a config file with this value stored as const, and import
  it from there
   */
  def checkIfTimeIsOk(screeningTime: Timestamp): Boolean = {
    screeningTime.compareTo(new Timestamp(System.currentTimeMillis() +900000)) > 0
  }

  /*
  This simple simple function aggregates two checks, performed by separate functions. If both checks are passed, it returns
  true
   */
  def checkName(name: String): Boolean = checkNameLength(name) && checkUppercase(name)

  /*
  This function is meant to check if name and surname starts with capital letter. As, in case of surname, it can be two-part
  name, separated with dash, the name is split using dash, and filtered by isLower - if every part of the name starts with
  capital letter.
  As it is not clear in business requirements, there is no limit on how many parts the surname can have. Same goes with
  the name. Therefore, if portuguese gentleman named Jorge Rodrigo Samuel Gonzalez Gomes Da Costa wants to book a ticket,
  he can use his full credentials, but has to put a dash between each part.
   */
  def checkUppercase(name:String): Boolean = !name.split("-").exists(_ (0).isLower)

  /*
  Just name length check. Nothing to elaborate
   */
  def checkNameLength(name:String): Boolean = name.length >= 3


  /*
  This function checks if all the requirements are met, for seats reservation.
  It takes two arguments - first is a list of seats from the order, other one is the current seat map, showing, which
  seats are occupied
  First, it checks if list of seats reservations is not empty.
  Then, it checks if any of the seats, the client wants to book, are not already booked - in this case, their position on
  the seatMap is not filled with 'X'
  Lastly, it checks, if, after the booking, there wil be no single seat gap. To do this, we use a helper function, that alters
  the seatMap, marking ordered seats as booked, and uses Regex pattern to check, if there is any single, unoccupied place
  between two occupied ones.

  NOTE: as business requirements only mentioned unocuppied seat between two occupied, it is currently possible to leave
  single seat on the edge of a row.

   */
  def checkSeats(seats: List[(Int, Int, Char)], seatsMap: Array[Array[Any]]): Boolean = {

    val acceptedTicketTypes = List('n','s','d')

    def willThereBeNoGaps(): Boolean = {
      seats.map(i => seatsMap(i._1)(i._2) = 'X')

      val pattern = "X-[0-9]*-X".r

      seatsMap.map(_.mkString("-")).exists(i => pattern.findAllIn(i).isEmpty)
    }

    if (!seats.filter(i => i._1 >= seatsMap.size || i._2 >= seatsMap(0).size).isEmpty) { //first we check, if there are
      println("hello")                                                                  // seats by this number
      false
    } else {(seats.nonEmpty                                            //order must contain at least one seat reservation
      && !seats.exists(i => seatsMap(i._1)(i._2) == 'X') // and none of the seats in order are already booked
      && willThereBeNoGaps()                                    // and there will be no single seat left between the occupied ones, after the order
      && !seats.exists(i => !acceptedTicketTypes.contains(i._3)))}  // and every ticket has an accepted type
  }

  def calculateTotalAmount(id: Int): Double = Await.result(db.run(amountQuery(id).result),2.seconds).sum

  val amountQuery: Int => Query[Rep[Double], Double, Seq] = id => for {
    r <- reservations if r.orderId === id
    t <- ticketRates if t.ticketType === r.ticketType
  } yield(t.charge)


}