package Humphrey.controllers

import Humphrey.controllers.screeningsController.screeningRep

import java.sql.Timestamp

object ControlUtils {
  val daysConverter = Map(0 -> "Niedziela",
    1 -> "Poniedziałek",
    2 -> "Wtorek",
    3 -> "Środa",
    4 -> "Czwartek",
    5 -> "Piątek",
    6 -> "Sobota")

  def resultToScreeningRep(i: (Timestamp, String, Int)): screeningRep  = i match {
    case (date,title,room) => screeningRep(date.toString,title,room)
  }
}
