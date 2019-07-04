package jorge.arada.orderManager

import akka.actor.{ActorRef, ActorSystem, Props}
import jorge.arada.orderManager.entities.{Lines, Order}

object Run extends App {


  val system: ActorSystem = ActorSystem("orderManagment")

  val orderLine = Lines(
    productId = "123",
    orderLineId = "1",
    quantity = 1,
    price = 10)

  val order = Order(
    orderId ="1",
    name = "Jorge",
    address = "maia",
    totalValue = 10,
    quantity = 1,
    orderLines = Seq(orderLine)
  )

  val orderProcessorActor: ActorRef = system.actorOf(Props[OrderProcessor])

  orderProcessorActor ! order

}
