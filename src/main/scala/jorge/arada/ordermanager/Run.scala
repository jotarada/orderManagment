package jorge.arada.ordermanager

import akka.actor.{ActorRef, ActorSystem, PoisonPill, Props}
import jorge.arada.ordermanager.entities.{Lines, Order}

object Run extends App {


  val system: ActorSystem = ActorSystem("orderManagment")

  val orderLine = Lines(
    productId = "123",
    orderLineId = "2",
    quantity = +2,
    price = 10)

  val order = Order(
    orderId ="1",
    name = "Jorge",
    address = "maia",
    totalValue = 10,
    quantity = 1,
    orderLines = Seq(orderLine)
  )

  val eventDispatcher: ActorRef = system.actorOf(Props[EventDispatcher],"eventDispatcher")
  val orderProcessorActor: ActorRef = system.actorOf(OrderProcessor.props(eventDispatcher),"orderProcessorActor")


  orderProcessorActor ! order


  Thread.sleep(2000)

  orderProcessorActor ! PoisonPill
  system.terminate()
}
