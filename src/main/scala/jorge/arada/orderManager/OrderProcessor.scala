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

  case class StockResult(
                          orderId: String,
                          orderLineId: String,
                          avaibility: Boolean
                        )

}

class OrderProcessor() extends Actor with ActorLogging {


  val stockValidatorActor: ActorRef = context.actorOf(Props[StockValidator])

  override def preStart(): Unit = {
    log.info("starting order processing")
  }

  override def receive: Receive = {
    case Order(orderId, name, address, totalValue, quantity, orderLines) =>
      orderLines.foreach(orderLine => stockValidatorActor ! Stock(orderId, orderLine.orderLineId, orderLine.productId, orderLine.quantity))


    case StockResult(orderId,orderLineId,resutl) =>
      if (resutl) {
        log.info(s"we have stock for the order $orderId line $orderLineId")
      }
      else {
        log.info(s"we don't stock for this order $orderId line $orderLineId")
      }
  }
}
