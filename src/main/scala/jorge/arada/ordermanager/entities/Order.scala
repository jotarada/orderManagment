package jorge.arada.ordermanager.entities

case class Order(
                orderId:String,
                name:String,
                address:String,
                totalValue:Double,
                quantity: Int,
                orderLines: Seq[Lines]
                )
