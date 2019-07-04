package jorge.arada.orderManager.entities

case class Lines(
                productId:String,
                orderLineId: String,
                quantity:Int,
                price:Double
                )