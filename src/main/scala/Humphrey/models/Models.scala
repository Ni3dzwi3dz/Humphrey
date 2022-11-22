package Humphrey.models

import slick.jdbc.SQLiteProfile.api._
import java.sql.Timestamp

import java.util.Date



object Models extends  {

  /**
   * Class representing a single movie, described by only basic parameters. As there is no intention of ex. validating if
   * the screenings don`t overlap, it should be just enough
   * @param tag - marks a specific row represented by an AbstractTable instance
   */
  class Movies(tag: Tag) extends Table[(Int,String,String)](tag, "MOVIES"){
    def movieId = column[Int]("movie_id",O.PrimaryKey)
    def title = column[String]("title")
    def director = column[String]("director")

    def * = (movieId,title,director)

  }
  val movies = TableQuery[Movies]

  class Rooms(tag: Tag) extends Table[(Int,Int,Int)](tag,"ROOMS"){
    val roomId = column[Int]("room_id", O.PrimaryKey)
    val rows = column[Int]("rows")
    val seats = column[Int]("")

    def * = (roomId,rows,seats)
  }

  val rooms = TableQuery[Rooms]

  case class Temp(date: Timestamp)

  class Screenings(tag: Tag) extends Table[(Int,Int,Int,Temp)](tag,"SCREENINGS"){
    val screeningId = column[Int]("screening_id",O.PrimaryKey)
    val movieId = column[Int]("movie_id")
    val roomId = column[Int]("room_id")
    val screeningTime = column[Timestamp]("screening_date")

    def * = (screeningId,movieId,roomId,screeningTime <> ((screeningDate: Timestamp) =>
      Temp.apply(screeningDate), Temp.unapply _))

    def movie = foreignKey("movie_fk", movieId, movies)(_.movieId)
    def room = foreignKey("room_fk",roomId,rooms)(_.roomId)
  }

  val screenings = TableQuery[Screenings]

  class Reservations(tag: Tag) extends Table[(Int,Int,Int,Int,Char)](tag,"RESERVATIONS"){
    val reservationId = column[Int]("reservation_id",O.PrimaryKey, O.AutoInc)
    val orderId = column[Int]("order_id")
    val row = column[Int]("row")
    val seat = column[Int]("seat")
    val ticketType = column[Char]("ticket_type")

    def * = (reservationId,orderId,row,seat,ticketType)

    def order = foreignKey("order_id",orderId,orders)(_.orderId)

  }
  val orders = TableQuery[Orders]



  class Orders(tag: Tag) extends Table[(Int,Int,String,String)](tag,"ORDERS"){
    val orderId = column[Int]("order_id",O.PrimaryKey, O.AutoInc)
    val screeningId = column[Int]("screening_id")
    val name = column[String]("name")
    val surname = column[String]("surname")

    def * = (orderId,screeningId,name,surname)

    def screening = foreignKey("screening_id",screeningId,screenings)(_.screeningId)

  }
  val reservations = TableQuery[Reservations]

  class TicketRates(tag: Tag) extends Table[(Int,Char,Double)](tag,"TICKET_RATES") {
    val rateId = column[Int]("rate_id", O.PrimaryKey, O.AutoInc)
    val ticketType = column[Char]("ticket_type")
    val charge = column[Double]("charge")

    def * = (rateId,ticketType,charge)
  }

  def ticketRates= TableQuery[TicketRates]





}

