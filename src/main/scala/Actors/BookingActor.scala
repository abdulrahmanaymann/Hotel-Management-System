package Actors

import Actors.BookingActor.defaultBooking

import scala.util.{Failure, Success, Try}
import akka.actor.{Actor, ActorLogging, Props}
import booking._
import Models.Booking


// Define messages for operations
object booking {
  case class CancelBooking()

  case class CheckIn()

  case class CheckOut(booking: Booking)

  case class GetTotalCost(id: Int)
}

private class BookingActor(initialBooking: Option[Booking] = None) extends Actor with ActorLogging {
  var booking: Booking = initialBooking.getOrElse(defaultBooking)

  log.info("BookingActor created.")

  override def preStart(): Unit = {
    log.info("BookingActor started.")
  }

  override def postStop(): Unit = {
    log.info("BookingActor stopped.")
  }

  override def receive: Receive = onMessage()

  private def onMessage(): Receive = {
    case CancelBooking() =>
      Try {
        this.booking.cancelBooking()
      } match {
        case Success(_) =>
          sender() ! log.info(s"Booking canceled successfully.")
        case Failure(ex) =>
          val errorMessage = s"Failed to cancel booking for ${booking.getBookingId}: ${ex.getMessage}"
          log.error(errorMessage)
          sender() ! errorMessage
      }

    case CheckIn() =>
      Try {
        this.booking.checkIn()
      } match {
        case Success(_) =>
          sender() ! log.info(s"Booking checked in successfully for ${booking.getBookingId}.")
        case Failure(ex) =>
          val errorMessage = s"Failed to check in booking for ${booking.getBookingId}: ${ex.getMessage}"
          log.error(errorMessage)
          sender() ! errorMessage
      }

    case CheckOut(booking) =>
      Try {
        booking.checkOut(initialBooking.getOrElse(defaultBooking))
      } match {
        case Success(_) =>
          sender() ! log.info(s"Booking checked out successfully.")
        case Failure(ex) =>
          val errorMessage = s"Failed to check out booking for ${booking.getBookingId}: ${ex.getMessage}"
          log.error(errorMessage)
          sender() ! errorMessage
      }

    case GetTotalCost(id) =>
      val result = Option(this.booking).map { b =>
        Try(b.calculateTotalCost()) match {
          case Success(totalCost) =>
            sender() ! log.info(s"Total cost calculated successfully: $totalCost for $id")
            totalCost
          case Failure(ex) =>
            val errorMessage = s"Failed to calculate total cost for $id: ${ex.getMessage}"
            log.error(errorMessage)
            sender() ! errorMessage
        }
      }.getOrElse {
        log.warning("Booking not found.")
        "Booking not found."
      }
      sender() ! result

    case _ =>
      val errorMessage = "Error: Invalid message."
      log.warning(errorMessage)
      sender() ! errorMessage
  }
}

object BookingActor {
  def props(initialBooking: Option[Booking] = None): Props = Props(new BookingActor(initialBooking))

  private val defaultBooking: Booking = new Booking(0, null, null, null, null, null)
}