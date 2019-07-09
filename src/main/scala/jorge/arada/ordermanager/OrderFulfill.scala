package jorge.arada.ordermanager

import akka.actor.{Actor, ActorLogging}
import jorge.arada.ordermanager.messages.{OrderFulfilled, OrderValidatedSuccess}

class OrderFulfill
  extends Actor with ActorLogging {

  override def preStart(): Unit = {

    log.info("starting orderFulfill")
  }

  override def receive: Receive = {

    case orderValidated: OrderValidatedSuccess =>

      orderValidated.order.orderLines.foreach(
        orderLine => {
          log.info(s"Stock hold for product ${orderLine.productId}")
        })
      log.info(s"Order ${orderValidated.order.orderId} fulfilled")

      sender ! OrderFulfilled(orderValidated.order)
  }
}
