package jorge.arada.ordermanager.repository

import akka.actor.{Actor, ActorLogging, Props}
import jorge.arada.ordermanager.messages.OpenFile

import scala.io.Source._

object FileReader {

  def props(fileName: String): Props = Props[FileReader](new FileReader(fileName))
}

class FileReader(fileName: String) extends Actor with ActorLogging {

  override def receive: Receive = {

    case OpenFile =>

      val data = fromFile(fileName)
      val arrayLines = data.getLines().toSeq
      data.close()

      sender ! arrayLines
  }
}
