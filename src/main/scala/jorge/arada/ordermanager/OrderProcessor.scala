package jorge.arada.ordermanager

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import jorge.arada.ordermanager.entities.Order
import jorge.arada.ordermanager.entities.Stock
import jorge.arada.ordermanager.messages.{OrderNotOk, OrderOk, StockAvaible, StockNotAvaible}

object OrderProcessor {

  def props(eventDispatcherActor: ActorRef): Props = Props[OrderProcessor](new OrderProcessor(eventDispatcherActor))

}

class OrderProcessor(eventDispatcherActor: ActorRef) extends Actor with ActorLogging {


  val stockValidatorActor: ActorRef = context.actorOf(Props[StockValidator], "stockValidatorActor")

  override def preStart(): Unit = {
    log.info("starting order processing")
  }

  override def receive: Receive = {
    case Order(orderId, name, address, totalValue, quantity, orderLines) =>
      orderLines.foreach(orderLine => stockValidatorActor ! Stock(orderId, orderLine.orderLineId, orderLine.productId, orderLine.quantity))

    case StockAvaible(stock) =>
      log.info(s"we have stock for the order ${stock.orderId} line ${stock.orderLineId}")
      eventDispatcherActor ! OrderOk

    case StockNotAvaible(stock) =>
      log.info(s"we don't stock for this order ${stock.orderId} line ${stock.orderLineId}")
      eventDispatcherActor ! OrderNotOk
  }
}
