package jorge.arada.orderManager

import akka.actor.{Actor, ActorLogging, Props}
import jorge.arada.orderManager.StockValidator.Stock
import jorge.arada.orderManager.OrderProcessor.StockResult

object StockValidator {

  def props(): Props = Props[StockValidator]

  case class Stock(
                    productId: String,
                    quantity: Int
                  )

}

class StockValidator extends Actor with ActorLogging {

  override def preStart(): Unit = {
    log.info("starting stockValidator")
  }

  override def receive: Receive = {

    case Stock(productId, quantity) =>
      if (quantity == 1) {
        sender() ! StockResult(true)
      }
      else {
        sender() ! StockResult(false)
      }

  }
}
