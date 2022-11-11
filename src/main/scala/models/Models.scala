package models

import slick.jdbc.SQLiteProfile.api._

object Models {

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

  class Screenings(tag: Tag) extends Table[(Int,Int,Int,Int,String)](tag,"SCREENINGS"){
    val screeningId = column[Int]("screening_id",O.PrimaryKey)
    val movieId = column[Int]("movie_id")
    val roomId = column[Int]("room_id")
    val day = column[Int]("day_of_the_week")
    val screeningTime = column[String]("screening_time")

    def * = (screeningId,movieId,roomId,day,screeningTime)

    def movie = foreignKey("movie_fk", movieId, movies)(_.movieId)
    def room = foreignKey("room_fk",roomId,rooms)(_.roomId)
  }

  val screenings = TableQuery[Screenings]

  class Orders(tag: Tag) extends Table[(Int,Int,Int,Int)](tag,"ORDERS"){
    val orderId = column[Int]("order_id",O.PrimaryKey)
    val screeningId = column[Int]("screening_id")
    val row = column[Int]("row")
    val seat = column[Int]("seat")

    def * = (orderId,screeningId,row,seat)

    def screening = foreignKey("screening_id",screeningId,screenings)(_.screeningId)

  }
}
