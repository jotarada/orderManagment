package jorge.arada.orderManager.entities

case class Order(
                name:String,
                address:String,
                totalValue:Double,
                quantity: Int,
                orderLines: Seq[Lines]
                )
