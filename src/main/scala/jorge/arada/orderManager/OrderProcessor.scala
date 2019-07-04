package jorge.arada.orderManager

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import jorge.arada.orderManager.OrderProcessor.StockResult
import jorge.arada.orderManager.entities.{Lines, Order}
import jorge.arada.orderManager.StockValidator._

object OrderProcessor {

  def props: Props = Props[OrderProcessor]

  case class Order(
                    name: String,
                    address: String,
                    totalValue: Double,
                    quantity: Int,
                    orderLines: Seq[Lines]
                  )

  case class StockResult(avaibility: Boolean)

}

class OrderProcessor() extends Actor with ActorLogging {


  val stockValidatorActor: ActorRef = context.actorOf(Props[StockValidator])

  override def preStart(): Unit = {
    log.info("starting order processing")
  }

  override def receive: Receive = {
    case Order(name, address, totalValue, quantity, orderLines) =>
      stockValidatorActor ! Stock(orderLines.head.productId, orderLines.head.quantity)

    case StockResult (resutl) =>
      if (resutl) {
        log.info("we have stock for this order")
      }
      else {
        log.info("we don't stock for this order")
      }
  }
}
