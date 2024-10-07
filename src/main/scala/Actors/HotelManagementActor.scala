package Actors

import scala.util.{Failure, Success, Try}
import akka.actor.{Actor, ActorLogging, Props}
import Models._
import Files.FileInsertExample._

// Messages
sealed trait HotelManagementMessage

case class AddCustomer(customer: Customer) extends HotelManagementMessage

case class AddRoom(room: Room) extends HotelManagementMessage

case class AddBooking(booking: Booking) extends HotelManagementMessage

case class RemoveCustomer(customer: Customer) extends HotelManagementMessage

case class RemoveRoom(room: Room) extends HotelManagementMessage

case class RemoveBooking(booking: Booking) extends HotelManagementMessage

case class FindCustomerById(id: Int) extends HotelManagementMessage

case class FindRoomById(id: Int) extends HotelManagementMessage

case class FindBookingById(id: Int) extends HotelManagementMessage

case class UpdateCustomer(customer: Customer) extends HotelManagementMessage

case class UpdateRoom(room: Room) extends HotelManagementMessage

case class UpdateBooking(booking: Booking) extends HotelManagementMessage

case class GenerateReport() extends HotelManagementMessage

case class GetCustomers() extends HotelManagementMessage

case class GetRooms() extends HotelManagementMessage

case class GetBooking() extends HotelManagementMessage

private class HotelManagementActor(hotelManagement: HotelManagement) extends Actor with ActorLogging {
  log.info("HotelManagementActor created.")

  override def preStart(): Unit = {
    log.info("HotelManagementActor started.")
  }

  override def postStop(): Unit = {
    log.info("HotelManagementActor stopped.")
  }

  override def receive: Receive = onMessage(hotelManagement)

  private def onMessage(hotelManagement: HotelManagement): Receive = {
    case AddCustomer(customer) =>
      val customersFilePath = "customers.txt"
      Try {
        val data = s"Customer_ID:${customer.getId} ,Customer_Name:${customer.getName}...\n"
        hotelManagement.addCustomer(customer)
        writeTOfile(customersFilePath, data)
        sender() ! log.info(s"Customer with ${customer.getId} added successfully.")

      } match {
        case Success(_) =>

          sender() ! log.info(s"Customer added successfully.")

        case Failure(ex) =>
          sender() ! log.error(s"Failed to add customer: ${ex.getMessage}")
      }

    case AddRoom(room) =>
      Try {
        val roomsFilePath = "rooms.txt"
        val data = s"Room_ID : ${room.getId} with price :" +
          s" ${room.getPrice}$$ with status: ${room.getRoomStatus} \n"
        writeTOfile(roomsFilePath, data)
        hotelManagement.addRoom(room)
        sender() ! log.info(s"Room with ${room.getId} added successfully to file-->$roomsFilePath .")
      } match {
        case Success(_) =>
          sender() ! log.info(s"Room added successfully.")

        case Failure(ex) =>
          sender() ! log.error(s"Failed to add room: ${ex.getMessage}")
      }

    case AddBooking(booking) =>
      Try {
        val bookingsFilePath = "bookings.txt"
        val data = s"Booking_ID : ${booking.getBookingId} from customer_ID : ${booking.getCustomer.getId} in Room_ID : ${booking.getRoom.getId}\n"
        writeTOfile(bookingsFilePath, data)
        hotelManagement.addBooking(booking)
      } match {
        case Success(_) =>
          sender() ! log.info(s"checked-in successfully with Booking_ID ${booking.getBookingId} .")

        case Failure(ex) =>
          sender() ! log.error(s"Failed to add booking: ${ex.getMessage}")
      }

    case RemoveCustomer(customer) =>
      Try {
        val customersFilePath = "customers.txt"
        val data = s"Customer_ID:${customer.getId} ,Customer_Name:${customer.getName}...\n"
        RemoveFromFile(data, customersFilePath)
        hotelManagement.removeCustomer(customer)
        sender() ! log.info("Customer removed successfully.")
      } match {
        case Success(_) =>
          sender() ! log.info(s"Customer removed successfully.")

        case Failure(ex) =>
          sender() ! log.error(s"Failed to remove customer: ${ex.getMessage}")
      }

    case RemoveRoom(room) =>
      Try {
        val roomsFilePath = "rooms.txt"
        val data = s"Room_ID : ${room.getId} with price :" +
          s" ${room.getPrice}$$ with status: ${room.getRoomStatus} \n"
        RemoveFromFile(data,roomsFilePath)
        hotelManagement.removeRoom(room)
      } match {
        case Success(_) =>
          sender() ! log.info(s"Room removed successfully.")

        case Failure(ex) =>
          sender() ! log.error(s"Failed to remove room: ${ex.getMessage}")
      }

    case RemoveBooking(booking) =>
      Try {
        val bookingsFilePath = "bookings.txt"
        val data = s"Booking_ID : ${booking.getBookingId} from customer_ID : ${booking.getCustomer.getId} in Room_ID : ${booking.getRoom.getId}\n"
        RemoveFromFile(bookingsFilePath, data)
        hotelManagement.removeBooking(booking)
      } match {
        case Success(_) =>
          sender() ! log.info(s"Checked-out removed successfully with booking ${booking.getBookingId}.")

        case Failure(ex) =>
          sender() ! log.error(s"Failed to remove booking: ${ex.getMessage}")
      }

    case FindCustomerById(id) =>
      Try {
        val customer = hotelManagement.findCustomerById(id)
        sender() ! log.info(s"Customer found successfully: $customer")
      } match {
        case Success(_) =>
          sender() ! log.info(s"Customer found successfully.")

        case Failure(ex) =>
          sender() ! log.error(s"Failed to find customer: ${ex.getMessage}")
      }

    case FindRoomById(id) =>
      Try {
        val room = hotelManagement.findRoomById(id)
        sender() ! log.info(s"Room found successfully: $room")
      } match {
        case Success(_) =>
          sender() ! log.info(s"Room found successfully.")

        case Failure(ex) =>
          sender() ! log.error(s"Failed to find room: ${ex.getMessage}")
      }

    case FindBookingById(id) =>
      Try {
        val booking = hotelManagement.findBookingById(id)
        sender() ! log.info(s"Booking found successfully: $booking")
      } match {
        case Success(_) =>
          sender() ! log.info(s"Booking found successfully.")

        case Failure(ex) =>
          sender() ! log.error(s"Failed to find booking: ${ex.getMessage}")
      }

    case UpdateCustomer(customer) =>
      Try {
        hotelManagement.updateCustomer(customer)
        sender() ! log.info("Customer updated successfully.")
      } match {
        case Success(_) =>
          sender() ! log.info(s"Customer updated successfully.")

        case Failure(ex) =>
          sender() ! log.error(s"Failed to update customer: ${ex.getMessage}")
      }

    case UpdateRoom(room) =>
      Try {
        hotelManagement.updateRoom(room)
        sender() ! log.info("Room updated successfully.")
      } match {
        case Success(_) =>
          sender() ! log.info("Room updated successfully.")

        case Failure(ex) =>
          sender() ! log.error(s"Failed to update room: ${ex.getMessage}")
      }

    case UpdateBooking(booking) =>
      Try {
        hotelManagement.updateBooking(booking)
        sender() ! log.info("Booking updated successfully.")
      } match {
        case Success(_) =>
          sender() ! log.info("Booking updated successfully.")

        case Failure(ex) =>
          sender() ! log.error(s"Failed to update booking: ${ex.getMessage}")
      }

    //    case MakeReservation(customer, room, checkInDate, checkOutDate) =>
    //      Try {
    //        hotelManagement.makeReservation(customer, room, checkInDate, checkOutDate)
    //        sender() ! log.info("Reservation made successfully.")
    //      } match {
    //        case Success(_) =>
    //          sender() ! log.info("Reservation made successfully.")
    //
    //        case Failure(ex) =>
    //          sender() ! log.error(s"Failed to make reservation: ${ex.getMessage}")
    //      }

    case GenerateReport() =>
      Try {
        hotelManagement.generateReport()
        sender() ! log.info("Report generated successfully.")
      } match {
        case Success(_) =>
          sender() ! log.info("Report generated successfully.")
        case Failure(ex) =>
          sender() ! log.error(s"Failed to generate report: ${ex.getMessage}")
      }

    case GetCustomers() =>
      Try {
        val customers = hotelManagement.getCustomers
        sender() ! log.info(s"Customers: $customers")
        sender() ! log.info("Retrieved customers successfully.")
      } match {
        case Success(_) =>
          sender() ! log.info("Retrieved customers successfully.")

        case Failure(ex) =>
          sender() ! log.error(s"Failed to retrieve customers: ${ex.getMessage}")
      }

    case GetRooms() =>
      Try {
        val rooms = hotelManagement.getRooms
        sender() ! log.info(s"Rooms: $rooms")
        sender() ! log.info("Retrieved rooms successfully.")
      } match {
        case Success(_) =>
          sender() ! log.info("Retrieved rooms successfully.")

        case Failure(ex) =>
          sender() ! log.error(s"Failed to retrieve rooms: ${ex.getMessage}")
      }

    case GetBooking() =>
      Try {
        val bookings = hotelManagement.getBookings
        sender() ! log.info(s"Bookings: $bookings")
        sender() ! log.info("Retrieved bookings successfully.")
      } match {
        case Success(_) =>
          sender() ! log.info("Retrieved bookings successfully.")

        case Failure(ex) =>
          sender() ! log.error(s"Failed to retrieve bookings: ${ex.getMessage}")
      }

    case _ =>
      sender() ! log.warning("Invalid message.")
  }
}

object HotelManagementActor {
  def props(hotelManagement: HotelManagement): Props = Props(new HotelManagementActor(hotelManagement))
}
