package Actors

import Actors.CustomerActor.defaultCustomer

import scala.util.{Failure, Success, Try}
import akka.actor.{Actor, ActorLogging, Props}
import Models.Customer

// Define messages for operations
case class UpdateCustomerInfo(newCustomer: Customer)

private class CustomerActor(initialCustomer: Option[Customer] = None) extends Actor with ActorLogging {
  var customer: Customer = initialCustomer.getOrElse(defaultCustomer)

  log.info("CustomerActor created.")

  override def preStart(): Unit = {
    log.info("CustomerActor started.")
  }

  override def postStop(): Unit = {
    log.info("CustomerActor stopped.")
  }

  override def receive: Receive = onMessage()

  private def onMessage(): Receive = {
    case UpdateCustomerInfo(newCustomer) =>
      Try {
        customer = newCustomer
        log.info(s"Customer updated: $customer")
        sender() ! customer
      } match {
        case Success(_) =>
          log.info("Success: Customer updated.")
        case Failure(e) =>
          val errorMessage = s"Error: Customer not updated: $e"
          log.warning(errorMessage)
          sender() ! errorMessage
      }

    case _ =>
      val errorMessage = "Error: Invalid message."
      log.warning(errorMessage)
      sender() ! errorMessage
  }
}

object CustomerActor {
  def props(initialCustomer: Option[Customer] = None): Props = Props(new CustomerActor(initialCustomer))

  private val defaultCustomer: Customer = new Customer(0, "Undefined")
}
