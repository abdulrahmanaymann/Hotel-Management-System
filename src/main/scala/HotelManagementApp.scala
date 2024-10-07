import scala.io.StdIn
import java.text.SimpleDateFormat
import java.util.Date
import Actors.{AddBooking, AddCustomer, AddRoom, BookingActor, CustomerActor, GenerateReport, GetBooking, GetCustomers, GetRooms, HotelManagementActor, RemoveCustomer, RemoveRoom, RoomActor, UpdateCustomer, UpdateRoom}
import Actors.booking.{CancelBooking, CheckIn, CheckOut}
import Enum.{BookingStatus, RoomStatus}
import Models.{Booking, Customer, HotelManagement, Room}
import akka.actor.{ActorSystem, Props}

import scala.util.{Failure, Random, Success, Try}

object HotelManagementApp extends App {
  // Initialize actors
  private val system = ActorSystem("HotelManagementSystem")
  val hotelManagement = HotelManagement()
   private val roomActor = system.actorOf(RoomActor.props(), "roomActor")
   private val customerActor = system.actorOf(CustomerActor.props(), "customerActor")
  private val bookingActor = system.actorOf(BookingActor.props(), "bookingActor")
  private val hotelManagementActor = system.actorOf(HotelManagementActor.props(hotelManagement), "hotelManagementActor")

  // Main menu
  while (true) {
    println("\nHotel Management System")
    println("1. Manage Customers")
    println("2. Manage Rooms")
    println("3. Manage Bookings")
    println("4. Generate Report")
    println("5. Exit")
    print("Enter your choice: ")

    val choice = StdIn.readInt()

    choice match {
      case 1 => manageCustomers()
      case 2 => manageRooms()
      case 3 => manageBookings()
      case 4 => hotelManagementActor ! GenerateReport()
      case 5 => System.exit(0)
      case _ => println("Invalid choice. Please try again.")
    }
  }

  def manageCustomers(): Unit = {
    println("\nManage Customers")
    println("1. Add Customer")
    println("2. Update Customer")
    println("3. Remove Customer")
    println("4. View Customers")
    println("5. Back to Main Menu")
    print("Enter your choice: ")

    val choice = StdIn.readInt()

    choice match {
      case 1 => addCustomer()
      case 2 => updateCustomer()
      case 3 => removeCustomer()
      case 4 => viewCustomers()
      case _ => println("Invalid choice. Please try again.")
    }
  }

  def addCustomer(): Unit = {
    Try {
      print("Enter customer name: ")
      val name = StdIn.readLine()
      val randomId = new Random().nextInt(100)
      val newCustomer = new Customer(randomId, name)
      hotelManagementActor ! AddCustomer(newCustomer)
    } match {
      case Success(_) =>
        println("Customer added successfully.")
      case Failure(ex) =>
        println(s"Failed to add customer: ${ex.getMessage}")
    }
  }

  def updateCustomer(): Unit = {
    print("Enter customer ID: ")
    val customerId = StdIn.readInt()
    val customer = hotelManagement.findCustomerById(customerId)

    customer.foreach { c =>
      print(s"Enter new name for customer ${c.getId}: ")
      val newName = StdIn.readLine()
      customerActor ! UpdateCustomer(c.copyWithNewInfo(newName))
    }
  }

  def removeCustomer(): Unit = {
    print("Enter customer ID: ")
    val customerId = StdIn.readInt()
    val customer = hotelManagement.findCustomerById(customerId)

    customer.foreach { c =>
      customerActor ! RemoveCustomer(c)
    }
  }

  def viewCustomers(): Unit = {
    hotelManagementActor ! GetCustomers()
  }

  def manageRooms(): Unit = {
    println("\nManage Rooms")
    println("1. Add Room")
    println("2. Update Room")
    println("3. Remove Room")
    println("4. View Rooms")
    println("5. Back to Main Menu")
    print("Enter your choice: ")

    val choice = StdIn.readInt()

    choice match {
      case 1 => addRoom()
      case 2 => updateRoom()
      case 3 => removeRoom()
      case 4 => viewRooms()
      case 5 =>
      case _ => println("Invalid choice. Please try again.")
    }
  }

  def addRoom(): Unit = {
    print("Enter room price: ")
    val price = StdIn.readDouble()
    val randomId = new Random().nextInt(100)
    roomActor ! AddRoom(new Room(randomId, price, RoomStatus.AVAILABLE))
  }

  def updateRoom(): Unit = {
    print("Enter room ID: ")
    val roomId = StdIn.readInt()
    val room = hotelManagement.findRoomById(roomId)

    room.foreach { r =>
      print(s"Enter new price for room ${r.getId}: ")
      val newPrice = StdIn.readDouble()
      roomActor ! UpdateRoom(r.copy(price = newPrice))
    }
  }

  def removeRoom(): Unit = {
    print("Enter room ID: ")
    val roomId = StdIn.readInt()
    val room = hotelManagement.findRoomById(roomId)

    room.foreach { r =>
      roomActor ! RemoveRoom(r)
    }
  }

  def viewRooms(): Unit = {
    hotelManagementActor ! GetRooms()
  }

  def manageBookings(): Unit = {
    println("\nManage Bookings")
    println("1. Make Reservation")
    println("2. Check-in")
    println("3. Check-out")
    println("4. Cancel Booking")
    println("5. View Bookings")
    println("6. Back to Main Menu")
    print("Enter your choice: ")

    val choice = StdIn.readInt()

    choice match {
      case 1 => makeReservation()
      case 2 => checkIn()
      case 3 => checkOut()
      case 4 => cancelBooking()
      case 5 => viewBookings()
      case 6 =>
      case _ => println("Invalid choice. Please try again.")
    }
  }

  def makeReservation(): Unit = {
    print("Enter customer ID: ")
    val customerId = StdIn.readInt()
    val customer = hotelManagement.findCustomerById(customerId)

    customer.foreach { c =>
      print("Enter room ID: ")
      val roomId = StdIn.readInt()
      val room = hotelManagement.findRoomById(roomId)

      room.foreach { r =>
        print("Enter check-in date (yyyy-MM-dd): ")
        val checkInStr = StdIn.readLine()
        val checkInDate = parseDate(checkInStr)

        print("Enter check-out date (yyyy-MM-dd): ")
        val checkOutStr = StdIn.readLine()
        val checkOutDate = parseDate(checkOutStr)

        val randomId = new Random().nextInt(100)
        hotelManagementActor ! AddBooking(new Booking(randomId, c, r, checkInDate, checkOutDate, BookingStatus.CHECKED_IN))
      }
    }
  }

  def checkIn(): Unit = {
    print("Enter booking ID: ")
    val bookingId = StdIn.readInt()
    val booking = hotelManagement.findBookingById(bookingId)

    booking.foreach(_ => bookingActor ! CheckIn())
  }

  def checkOut(): Unit = {
    print("Enter booking ID: ")
    val bookingId = StdIn.readInt()
    val booking = hotelManagement.findBookingById(bookingId)

    booking.foreach(b => bookingActor ! CheckOut(b))
  }

  def cancelBooking(): Unit = {
    print("Enter booking ID: ")
    val bookingId = StdIn.readInt()
    val booking = hotelManagement.findBookingById(bookingId)

    booking.foreach(_ => bookingActor ! CancelBooking())
  }

  def viewBookings(): Unit = {
    hotelManagementActor ! GetBooking()
  }

  def parseDate(dateStr: String): Date = {
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd")
    dateFormat.parse(dateStr)
  }
}
