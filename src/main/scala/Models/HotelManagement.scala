package Models

import Actors.CustomerActor
import Enum.{BookingStatus, RoomStatus}

import java.util.{Calendar, Date}

class HotelManagement {
  private var customers: List[Customer] = List()
  private var rooms: List[Room] = List()
  private var bookings: List[Booking] = List()
  // booking for remove
  private var booking: List[Booking] = List()
  // room for remove
  private var room: List[Room] = List()

  // Constructor
  def this(customers: List[Customer], rooms: List[Room], bookings: List[Booking]) = {
    this()
    this.customers = customers
    this.rooms = rooms
    this.bookings = bookings
  }

  // Getters
  def getCustomers: List[Customer] = this.customers

  def getRooms: List[Room] = this.rooms

  def getBookings: List[Booking] = this.bookings

  // Setters
  private def setCustomers(customers: List[Customer]): Unit = this.customers = customers

  private def setRooms(rooms: List[Room]): Unit = this.rooms = rooms

  private def setBookings(bookings: List[Booking]): Unit = this.bookings = bookings

  // CRUD operations for customers, rooms, and bookings
  def addCustomer(customer: Customer): Unit = this.customers = this.customers :+ customer

  def addRoom(room: Room): Unit = {
    this.rooms = this.rooms :+ room
    this.room = this.room :+ room
  }

  def addBooking(booking: Booking): Unit = {

    this.bookings = this.bookings :+ booking
    this.booking = this.booking :+ booking
  }

  def removeCustomer(customer: Customer): Unit = this.customers = this.customers.filterNot(_ == customer)

  def removeRoom(room: Room): Unit = {
    room.updateRoomStatus(RoomStatus.AVAILABLE)
    this.room = this.room.filterNot(_ == room)
  }

  def removeBooking(booking: Booking): Unit = {

    this.booking = this.booking.filterNot(_ == booking)
  }

  def findCustomerById(id: Int): Option[Customer] = this.customers.find(_.getId == id)

  def findRoomById(id: Int): Option[Room] = this.rooms.find(_.getId == id)

  def findBookingById(id: Int): Option[Booking] = this.bookings.find(_.getBookingId == id)

  def updateCustomer(customer: Customer): Unit = {
    val index = this.customers.indexWhere(_.getId == customer.getId)
    this.customers = this.customers.updated(index, customer)
  }

  def updateRoom(room: Room): Unit = {
    val index = this.rooms.indexWhere(_.getId == room.getId)
    if (index != -1) {
      this.rooms = this.rooms.updated(index, room)
    } else {
      // Consider throwing an exception or returning a failure result
      throw new IllegalArgumentException(s"Error: Room with ID ${room.getId} not found.")
    }
  }


  def updateBooking(booking: Booking): Unit = {
    val index = this.bookings.indexWhere(_.getBookingId == booking.getBookingId)
    this.bookings = this.bookings.updated(index, booking)
  }


  def generateReport(): Unit = {

    // Get the total number of rooms
    val totalRooms = this.room.length

    // Get the total number of rooms available
    val totalRoomsAvailable = this.rooms.count(_.isAvailable)

    // Get the total number of rooms reserved
    val totalRoomsReserved = this.rooms.count(_.isReserved)

    // Get the total number of rooms under maintenance
    val totalRoomsMaintenance = this.rooms.count(_.isMaintenance)

    // Get the total number of customers
    val totalCustomers = this.customers.length

    // Get the total number of bookings
    val totalBookings = this.booking.length

    // Get the total number of bookings checked in
    val totalBookingsCheckedIn = this.bookings.count(_.getStatus == BookingStatus.CHECKED_IN)

    // Get the total number of bookings checked out
    val totalBookingsCheckedOut = this.bookings.count(_.getStatus == BookingStatus.CHECKED_OUT)


    // Get the total number of bookings canceled
    val totalBookingsCanceled = this.bookings.count(_.getStatus == BookingStatus.CANCELED)

    // Get the total revenue
    val totalRevenue = this.bookings.map(_.calculateTotalCost()).sum

    // Print the report
    println("Hotel Management Report")
    println("Total Rooms: " + totalRooms)
    println("Total Rooms Available: " + totalRoomsAvailable)
    println("Total Rooms Reserved: " + totalRoomsReserved)
    println("Total Rooms Under Maintenance: " + totalRoomsMaintenance)
    println("Total Customers: " + totalCustomers)
    println("Total Bookings: " + totalBookings)
    println("Total Bookings Checked In: " + totalBookingsCheckedIn)
    println("Total Bookings Checked Out: " + totalBookingsCheckedOut)
    println("Total Bookings Canceled: " + totalBookingsCanceled)
    println("Total Revenue: " + totalRevenue)
  }

}

object HotelManagement {
  def apply(): HotelManagement = new HotelManagement()
}