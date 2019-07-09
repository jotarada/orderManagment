package jorge.arada.ordermanager.messages

import jorge.arada.ordermanager.entities.Order

case class OrderValidatedNotSuccess(
  order: Order,
  reason: String
)
