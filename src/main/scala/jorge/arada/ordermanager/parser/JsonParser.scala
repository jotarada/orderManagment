package jorge.arada.ordermanager.parser

import akka.actor.{Actor, Props}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import jorge.arada.ordermanager.entities.Order
import jorge.arada.ordermanager.messages.MessagesToDeserialize

object JsonParser {

  def props(mapper: ObjectMapper with ScalaObjectMapper): Props = Props[JsonParser](new JsonParser(mapper))
}

class JsonParser(mapper: ObjectMapper with ScalaObjectMapper)
  extends Actor {

  mapper.registerModule(DefaultScalaModule)

  override def receive: Receive = {

    case lines: MessagesToDeserialize[_] => val a  = lines.message.map(m => {val x = mapper.readValue(m, lines.tipo)})
  }
}
