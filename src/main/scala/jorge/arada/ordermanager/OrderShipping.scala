package jorge.arada.ordermanager

import akka.actor.{Actor, ActorLogging}
import jorge.arada.ordermanager.messages.{OrderFulfilled, OrderShipped}

class OrderShipping
  extends Actor with ActorLogging {

  override def preStart(): Unit = {

    log.info("starting orderShipping")
  }

  override def receive: Receive = {

    case orderToShip: OrderFulfilled =>

      log.info(s"Order ${orderToShip.order.orderId} Shipped")

      sender ! OrderShipped(orderToShip.order)
  }
}
