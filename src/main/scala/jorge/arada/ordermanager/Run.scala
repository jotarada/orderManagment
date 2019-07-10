package jorge.arada.ordermanager

import akka.actor.{ActorRef, ActorSystem, PoisonPill, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import jorge.arada.ordermanager.entities.{Lines, Order}
import jorge.arada.ordermanager.messages.{MessagesToDeserialize, OpenFile}
import jorge.arada.ordermanager.parser.JsonParser
import jorge.arada.ordermanager.repository.{EventDispatcher, EventRepository, FileReader}

import scala.concurrent.Await
import scala.concurrent.duration._

object Run
  extends App {

  val system: ActorSystem = ActorSystem("orderManagment")

  val orderLine = Lines(
    productId = "123",
    orderLineId = "2",
    quantity = 1,
    price = 10)

  val order = Order(
    orderId = "1",
    userId = 1,
    name = "Jorge",
    address = "maia",
    totalValue = 10,
    quantity = 1,
    orderLines = Seq(orderLine)
  )

  implicit val timeout = Timeout(1 minute)
  val file = getClass.getClassLoader.getResource("orders.json").getPath

  val fileReader: ActorRef = system.actorOf(FileReader.props(file), "fileReaderActor")
  val jsonParserActor: ActorRef = system
    .actorOf(JsonParser.props(new ObjectMapper with ScalaObjectMapper), "jsonParserActor")

  val future = fileReader ? OpenFile
  val orders = Await.result(future, timeout.duration).asInstanceOf[Seq[String]]

  val eventDispatcher: ActorRef = system.actorOf(Props[EventDispatcher], "eventDispatcher")
  val eventRepository: ActorRef = system.actorOf(Props[EventRepository], "eventRepository")
  val orderProcessorActor: ActorRef = system
    .actorOf(OrderProcessor.props(eventRepository, eventDispatcher), "orderProcessorActor")

  jsonParserActor ! MessagesToDeserialize[Order](orders,classOf[Order])
  orderProcessorActor ! order

  Thread.sleep(2000)

  orderProcessorActor ! PoisonPill
  system.terminate()
}
