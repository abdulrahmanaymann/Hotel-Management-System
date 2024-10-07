package Actors

import Actors.RoomActor.defaultRoom
import Enum.RoomStatus

import scala.util.{Failure, Success, Try}
import akka.actor.{Actor, ActorLogging, Props}
import Models.Room

// Define messages for operations
case class UpdateRoomStatus(updatedRoom: Room)
case class UpdateRoomPrice(updatedRoom: Room)
case class IsAvailable(dummy: Room)
case class IsReserved(dummy: Room)
case class RequestMaintenance()

class RoomActor(initialRoom: Option[Room] = None) extends Actor with ActorLogging {
  var room: Room = initialRoom.getOrElse(defaultRoom)

  log.info("RoomActor created.")

  override def preStart(): Unit = {
    log.info("RoomActor started.")
  }

  override def postStop(): Unit = {
    log.info("RoomActor stopped.")
  }

  override def receive: Receive = {
    case UpdateRoomStatus(updatedRoom) =>
      Try {
        this.room.updateRoomStatus(updatedRoom.getRoomStatus)
        log.info("Room status updated successfully.")
        sender() ! "Room status updated successfully."
      } match {
        case Success(_) =>
          sender() ! "Room status updated successfully."

        case Failure(ex) =>
          val errorMessage = s"Failed to update room status: ${ex.getMessage}"
          log.error(errorMessage)
          sender() ! errorMessage
      }

    case UpdateRoomPrice(updatedRoom) =>
      Try {
        this.room.updateRoomPrice(updatedRoom.getPrice)
        log.info("Room price updated successfully.")
        sender() ! "Room price updated successfully."
      } match {
        case Failure(ex) =>
          val errorMessage = s"Failed to update room price: ${ex.getMessage}"
          log.error(errorMessage)
          sender() ! errorMessage
      }

    case IsAvailable(_) =>
      Try {
        this.room.isAvailable
        log.info("Room is Available.")
        sender() ! "Room is Available."
      } match {
        case Success(_) =>
          sender() ! "Room is Available."

        case Failure(ex) =>
          val errorMessage = s"Failed to check if room is Available: ${ex.getMessage}"
          log.error(errorMessage)
          sender() ! errorMessage
      }

    case IsReserved(_) =>
      Try {
        this.room.isReserved
        log.info("Room is Reserved.")
        sender() ! "Room is Reserved."
      } match {
        case Success(_) =>
          sender() ! "Room is Reserved."

        case Failure(ex) =>
          val errorMessage = s"Failed to check if room is Reserved: ${ex.getMessage}"
          log.error(errorMessage)
          sender() ! errorMessage
      }

    case RequestMaintenance() =>
      Try {
        this.room.requestMaintenance()
        log.info("Room is under maintenance.")
        sender() ! "Room is under maintenance."
      } match {
        case Success(_) =>
          sender() ! "Room is under maintenance."

        case Failure(ex) =>
          val errorMessage = s"Failed to check if room is under maintenance.: ${ex.getMessage}"
          log.error(errorMessage)
          sender() ! errorMessage
      }

    case _ =>
      log.warning("Error: Invalid message.")
      sender() ! "Error: Invalid message."
  }
}

object RoomActor {
  def props(initialRoom: Option[Room] = None): Props = Props(new RoomActor(initialRoom))
  private val defaultRoom: Room = new Room(0, 0.0, RoomStatus.AVAILABLE)
}
