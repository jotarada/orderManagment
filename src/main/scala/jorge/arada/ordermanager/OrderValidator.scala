package jorge.arada.ordermanager

import akka.actor.{Actor, ActorLogging, Props}
import jorge.arada.ordermanager.entities.Order
import jorge.arada.ordermanager.messages.{OrderValidatedNotSuccess, OrderValidatedSuccess}

object OrderValidator {

  def props(): Props = Props[OrderValidator]
}

class OrderValidator
  extends Actor with ActorLogging {

  override def preStart(): Unit = {

    log.info("starting orderValidator")
  }

  override def receive: Receive = {

    case order: Order =>

      order.orderLines.foreach(
        orderLine => {
          if(orderLine.quantity == 1) {
            sender() ! OrderValidatedSuccess(order)
          }
          else {
            sender() ! OrderValidatedNotSuccess(order, "No Stock")
          }
        })
  }
}
