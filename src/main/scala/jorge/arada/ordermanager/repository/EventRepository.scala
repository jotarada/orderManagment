package jorge.arada.ordermanager.repository

import akka.actor.{Actor, ActorLogging, Props}
import jorge.arada.ordermanager.messages._

object EventRepository {

  def props: Props = Props[EventRepository]
}

class EventRepository
  extends Actor with ActorLogging {

  override def receive: Receive = {

    case orderReceived: OrderReceived =>
      log.info(s"Order ${orderReceived.order.orderId} received with success")

    case OrderValidatedSuccess(order) =>
      log.info(
        s"Order valid ${order.orderId} line ${order.orderLines.head.orderLineId} product ${
          order.orderLines.head.productId
        }")

    case OrderValidatedNotSuccess(order, reason) =>
      log.info(
        s"Invalid order ${order.orderId} line ${order.orderLines.head.orderLineId} with ${
          order.orderLines.head.productId
        } because $reason")

    case orderFulfilled: OrderFulfilled =>
      log.info(s"Fulfill success for order ${orderFulfilled.order.orderId}")

    case orderShipped: OrderShipped =>
      log.info(s"shipped success for order ${orderShipped.order.orderId}")

    case other: AnyRef => log.info(s"Not know how to handle this ${other.getClass.toString}")
  }
}
