package Enum

object RoomStatus extends Enumeration {
  type RoomStatus = Value
  val AVAILABLE, RESERVED, MAINTENANCE = Value
}
