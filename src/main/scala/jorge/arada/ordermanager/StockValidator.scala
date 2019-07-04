package jorge.arada.ordermanager

import akka.actor.{Actor, ActorLogging, Props}
import jorge.arada.ordermanager.entities.Stock
import jorge.arada.ordermanager.messages.{StockAvaible, StockNotAvaible}

object StockValidator {

  def props(): Props = Props[StockValidator]

}

class StockValidator extends Actor with ActorLogging {

  override def preStart(): Unit = {
    log.info("starting stockValidator")
  }

  override def receive: Receive = {

    case stock: Stock =>
      if (stock.quantity == 1) {
        sender() ! StockAvaible(stock)
      }
      else {
        sender() ! StockNotAvaible(stock)
      }

  }
}
