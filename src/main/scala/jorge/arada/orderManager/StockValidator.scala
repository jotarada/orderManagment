package jorge.arada.orderManager

import akka.actor.{Actor, ActorLogging, Props}
import jorge.arada.orderManager.StockValidator.Stock
import jorge.arada.orderManager.OrderProcessor.StockResult

object StockValidator {

  def props(): Props = Props[StockValidator]

  case class Stock(
                    orderId: String,
                    orderLineId: String,
                    productId: String,
                    quantity: Int
                  )

}

class StockValidator extends Actor with ActorLogging {

  override def preStart(): Unit = {
    log.info("starting stockValidator")
  }

  override def receive: Receive = {

    case Stock(orderId,orderLineId,productId, quantity) =>
      if (quantity == 1) {
        sender() ! StockResult(orderId,orderLineId,true)
      }
      else {
        sender() ! StockResult(orderId,orderLineId,false)
      }

  }
}
