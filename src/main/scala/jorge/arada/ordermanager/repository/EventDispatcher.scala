package jorge.arada.ordermanager.repository

import akka.actor.{Actor, ActorLogging, Props}
import jorge.arada.ordermanager.messages.{OrderNotOk, OrderOk}

object EventDispatcher {
  def props: Props = Props[EventDispatcher]
}

class EventDispatcher extends Actor with ActorLogging{


  override def receive: Receive =
  {
    case OrderOk => log.info("Not implemented")
    case OrderNotOk => log.info("Not implemented")
  }
}
