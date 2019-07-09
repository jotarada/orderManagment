package jorge.arada.ordermanager.entities

case class Order(
  orderId: String,
  userId: Int,
  name: String,
  address: String,
  totalValue: Double,
  quantity: Int,
  orderLines: Seq[Lines]
)
