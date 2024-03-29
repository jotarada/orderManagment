package jorge.arada.ordermanager

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import jorge.arada.ordermanager.entities.Order
import jorge.arada.ordermanager.messages._

object OrderProcessor {

  def props(eventRepository: ActorRef, eventDispatcherActor: ActorRef): Props = Props[OrderProcessor](new OrderProcessor(eventRepository, eventDispatcherActor))
}

class OrderProcessor(eventRepository: ActorRef, eventDispatcher: ActorRef)
  extends Actor with ActorLogging {

  val orderValidatorActor: ActorRef = context.actorOf(Props[OrderValidator], "orderValidatorActor")
  val orderFulFillActor: ActorRef = context.actorOf(Props[OrderFulfill], "orderFulFillActor")
  val orderShippingActor: ActorRef = context.actorOf(Props[OrderShipping], "orderShippingActor")

  override def preStart(): Unit = {

    log.info("starting order processing")
  }

  override def receive: Receive = {

    case order: Order =>
      eventRepository ! OrderReceived(order)

      orderValidatorActor ! order

    case orderValidatedSuccess: OrderValidatedSuccess =>
      log.info(
        s"we validate with sucess ${orderValidatedSuccess.order.orderId} line ${
          orderValidatedSuccess.order.orderLines.head.orderLineId
        }")
      eventRepository ! orderValidatedSuccess
      //TODO: stock hold should be done one validator. FulFill should be from outside
      orderFulFillActor ! orderValidatedSuccess

    case orderValidatedNotSuccess: OrderValidatedNotSuccess =>
      log.info(
        s"we failed to validate ${orderValidatedNotSuccess.order.orderId} line ${
          orderValidatedNotSuccess.order.orderLines.head.orderLineId
        }")
      eventRepository ! orderValidatedNotSuccess

    case orderFulFilled: OrderFulfilled =>
      log.info(
        s"Fulfill sucess ${orderFulFilled.order.orderId}")
      eventRepository ! orderFulFilled
      //TODO: Ship should be from outside
      orderShippingActor ! orderFulFilled

    case orderShiped: OrderShipped =>
      log.info(
        s"Shipped with sucess ${orderShiped.order.orderId}")
      eventRepository ! orderShiped
  }
}

