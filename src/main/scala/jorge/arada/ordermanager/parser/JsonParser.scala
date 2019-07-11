package jorge.arada.ordermanager.parser

import akka.actor.{Actor, Props}
import jorge.arada.ordermanager.entities.Entities

object JsonParser {

  def props(): Props = Props[JsonParser]
}

class JsonParser()
  extends Actor {

  override def receive: Receive = {

    case message: (Seq[String], String => Entities) => val entities: Seq[Entities] = message._1.map(m => message._2(m))
      sender() ! entities
  }
}
