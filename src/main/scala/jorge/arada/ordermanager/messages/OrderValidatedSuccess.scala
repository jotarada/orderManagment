package jorge.arada.ordermanager.messages

import jorge.arada.ordermanager.entities.Order

case class OrderValidatedSuccess(
  order: Order
)
