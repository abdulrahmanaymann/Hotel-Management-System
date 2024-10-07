package Enum

object BookingStatus extends Enumeration {
  type BookingStatus = Value
  val CHECKED_IN, CHECKED_OUT, CANCELED = Value
}
