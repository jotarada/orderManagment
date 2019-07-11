package jorge.arada.ordermanager

import akka.actor.{ActorRef, ActorSystem, PoisonPill, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import jorge.arada.ordermanager.entities.Order
import jorge.arada.ordermanager.messages.OpenFile
import jorge.arada.ordermanager.parser.JsonParser
import jorge.arada.ordermanager.repository.{EventDispatcher, EventRepository, FileReader}

import scala.concurrent.Await
import scala.concurrent.duration._

object Run
  extends App {

  implicit val timeout: Timeout = Timeout(1 minute)
  val file = getClass.getClassLoader.getResource("orders.json").getPath

  def processOrders[T](value: String)(implicit m: Manifest[T]): T = {

    val mapper = new ObjectMapper with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    mapper.readValue[T](value)
  }

  def readFromFile(file: String): Seq[String] = {

    val fileReader: ActorRef = system.actorOf(FileReader.props(file), "fileReaderActor")
    val future = fileReader ? OpenFile
    val ordersJson = Await.result(future, timeout.duration).asInstanceOf[Seq[String]]
    fileReader ! PoisonPill
    ordersJson
  }

  def deserialize[T: Manifest](jsonParserActor: ActorRef): Seq[T] = {

    val orderToDeserialize: (Seq[String], String => T) = (readFromFile(file), processOrders[T])
    val futureOrder = jsonParserActor ? orderToDeserialize
    Await.result(futureOrder, timeout.duration).asInstanceOf[Seq[T]]
  }

  val system: ActorSystem = ActorSystem("orderManagment")

  val jsonParserActor: ActorRef = system.actorOf(Props[JsonParser], "jsonParserActor")
  val eventDispatcher: ActorRef = system.actorOf(Props[EventDispatcher], "eventDispatcher")
  val eventRepository: ActorRef = system.actorOf(Props[EventRepository], "eventRepository")
  val orderProcessorActor: ActorRef = system.actorOf(OrderProcessor.props(eventRepository, eventDispatcher), "orderProcessorActor")

  deserialize[Order](jsonParserActor).foreach(order => orderProcessorActor ! order)

  Thread.sleep(2000)

  orderProcessorActor ! PoisonPill
  system.terminate()
}
