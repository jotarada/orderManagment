package jorge.arada.ordermanager.entities

case class Stock(
                  orderId: String,
                  orderLineId: String,
                  productId: String,
                  quantity: Int
                )