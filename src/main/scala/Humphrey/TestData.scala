package Humphrey

import java.sql.Timestamp

import models.Models.Temp

object TestData {

  val moviesInsert = Seq(
    (1, "Django", "Quentin Tarantino"),
    (2, "Se7en", "David Fincher"),
    (3, "Miś", "Stanisław Bareja"),
    (4, "Coś", "John Carpenter")
  )

  val roomsInsert = Seq(
    (1, 9, 10),
    (2, 10, 10),
    (3, 5, 5)
  )

  val screeningsInsert = Seq(
    (1, 1,  1, Temp(Timestamp.valueOf("2022-11-15 19:15:00"))),
    (2, 2,  2, Temp(Timestamp.valueOf("2022-11-15 19:15:00"))),
    (3, 3,  1, Temp(Timestamp.valueOf("2022-11-15 15:15:00"))),
    (4, 4,  3, Temp(Timestamp.valueOf("2022-11-16 22:15:00"))),
    (5, 1,  3, Temp(Timestamp.valueOf("2022-11-15 16:15:00"))),
    (6, 2,  1, Temp(Timestamp.valueOf("2022-11-15 17:15:00"))),
    (7, 3,  2, Temp(Timestamp.valueOf("2022-11-15 18:15:00"))),
    (8, 4,  1, Temp(Timestamp.valueOf("2022-11-16 21:15:00"))),
  )

  val ordersInsert = Seq(
    (1,1, "Grzegorz", "Brzęczyszczykiewicz"),
    (2,1, "Róża", "Gżegżółka"),
    (3,2, "Lechosłań", "Brzęczywół")
  )

  val reservationsInsert = Seq(
    (1,1,3,4,'n'),
    (2,1,3,5,'n'),
    (3,2,1,1,'n'),
    (4,2,1,2,'d'),
    (5,3,3,3,'n'),
    (6,3,3,4,'d'),
    (7,3,3,2,'d')

  )

}
