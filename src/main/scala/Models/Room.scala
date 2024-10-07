package Models

import Enum.RoomStatus._

class Room(
            private val id: Int,
            private var price: Double,
            private var roomStatus: RoomStatus,
          ) {

  private var maintenanceRequested: Boolean = false

  // Getters
  def getId: Int = id

  def getPrice: Double = price

  def getRoomStatus: RoomStatus = roomStatus

  def getMaintenanceRequested: Boolean = maintenanceRequested


  // Setters
  def setPrice(price: Double): Unit = this.price = price

  def setRoomStatus(roomStatus: RoomStatus): Unit = this.roomStatus = roomStatus


  def setMaintenanceRequested(maintenanceRequested: Boolean): Unit = this.maintenanceRequested = maintenanceRequested

  // toString
  override def toString: String = s"Room($id, $price, $roomStatus)"

  def updateRoomStatus(newStatus: RoomStatus): Unit = {
    roomStatus = newStatus
  }

  def updateRoomPrice(newPrice: Double): Unit = {
    try {
      if (newPrice >= 0) {
        price = newPrice
      } else {
        throw new IllegalArgumentException("Price cannot be negative.")
      }
    } catch {
      case e: Exception =>
        println(s"Error updating room price: $e")
    }
  }


  def isAvailable: Boolean = roomStatus == AVAILABLE

  def isReserved: Boolean = roomStatus == RESERVED

  def isMaintenance: Boolean = roomStatus == MAINTENANCE

  def requestMaintenance(): Unit = {
    try {
      if (!maintenanceRequested) {
        maintenanceRequested = true
        updateRoomStatus(MAINTENANCE)
        println(s"Maintenance requested for Room $getId, so room status now is $getRoomStatus")
      } else {
        throw new IllegalStateException("Maintenance is already requested.")
      }
    } catch {
      case e: Exception =>
        println(s"Error requesting maintenance: $e")
    }
  }

  def copy(
            id: Int = this.id,
            price: Double = this.price,
            roomStatus: RoomStatus = this.roomStatus
          ): Room = {
    new Room(id, price, roomStatus)
  }
}

object Room {
  def apply(id: Int, price: Double, roomStatus: RoomStatus): Room = new Room(id, price, roomStatus)
}
