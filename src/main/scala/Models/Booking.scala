package Models

import java.util.Date
import Enum.BookingStatus
import Enum.BookingStatus._

class Booking(
               private val bookingId: Int,
               private val customer: Customer,
               private val room: Room,
               private val checkInDate: Date,
               private val checkOutDate: Date,
               private var status: BookingStatus
             ) {

  // Getters
  def getBookingId: Int = bookingId

  def getCustomer: Customer = customer

  def getRoom: Room = room

  def getCheckInDate: Date = checkInDate

  def getCheckOutDate: Date = checkOutDate

  def getStatus: BookingStatus = status

  def setBookingStatus(bookingStatus: BookingStatus): Unit = this.status = bookingStatus


  // toString
  override def toString: String = s"Booking($bookingId, $customer, $room, $checkInDate, $checkOutDate, $status)"

  def calculateTotalCost(): Double = {
    try {
      val days = (checkOutDate.getTime - checkInDate.getTime) / (1000 * 60 * 60 * 24)
      days * room.getPrice
    } catch {
      case e: Exception =>
        println(s"Error calculating total cost: $e")
        0.0
    }
  }

  def cancelBooking(): Unit = {
    try {
      if (status != BookingStatus.CANCELED) {
        status = BookingStatus.CANCELED
      } else {
        throw new IllegalStateException("Booking is already canceled.")
      }
    } catch {
      case e: Exception =>
        println(s"Error canceling booking: $e")
    }
  }

  def checkIn(): Unit = {
    try {
      if (status == BookingStatus.CHECKED_IN) {
        throw new IllegalStateException("Booking is already checked in.")
      }
      status = BookingStatus.CHECKED_IN
      println("Checked-in successfully :)-")
    } catch {
      case e: Exception =>
        println(s"Error checking in: $e")
    }
  }

  def checkOut(booking: Booking): Unit = {
    try {
      if (booking.getStatus == BookingStatus.CHECKED_OUT) {
        throw new IllegalStateException("Booking is already checked out.")
      }
      setBookingStatus(BookingStatus.CHECKED_OUT)
      println(s"Checked out successfully for ${booking.bookingId} ")
    } catch {
      case e: Exception =>
        println(s"Error checking out: $e")
    }
  }
}

object Booking {
  def apply(
             bookingId: Int,
             customer: Customer,
             room: Room,
             checkInDate: Date,
             checkOutDate: Date,
             status: BookingStatus
           ): Booking = new Booking(bookingId, customer, room, checkInDate, checkOutDate, status)
}
