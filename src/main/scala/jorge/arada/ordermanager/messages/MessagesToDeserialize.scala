package jorge.arada.ordermanager.messages

import scala.reflect.runtime.universe._

case class MessagesToDeserialize[T](message: Seq[String],
  tipo: Class[T])

