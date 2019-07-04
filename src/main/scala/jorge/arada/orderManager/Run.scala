package jorge.arada.orderManager

import akka.actor.{ActorRef, ActorSystem, Props}
import jorge.arada.orderManager.entities.{Lines, Order}
import OrderProcessor._

object Run extends App {



  val system: ActorSystem = ActorSystem("orderManagment")

  val orderLine = Lines(
    productId = "123",
    quantity = 1,
    price = 10)

  val order = Order(
    name = "Jorge",
    address = "maia",
    totalValue = 10,
    quantity = 1,
    orderLines = Seq(orderLine)
  )

  val orderProcessorActor: ActorRef = system.actorOf(Props[OrderProcessor])

  orderProcessorActor ! order

}
