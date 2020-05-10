package warehouse

import warehouse.objects._
object Layout {
    // CURRENT LAYOUT:
    val doors:List[Door] = List(new Door(528,0),new Door(529,0))
    val products:Map[Int, Product] = Map(
      1000 -> new Product(1000, "CHARCOAL 20#", 30, 20.00),
      1001 -> new Product(1001, "CHARCOAL 10#", 56, 10.00)
    )

    var aisles:Map[Int, Aisle] = Map(
      596 -> new Aisle(596, 30)
    )

    aisles(596).items = Map(
      1010 -> new Pickslot(596, 1010,1000,30),
      1011 -> new Pickslot(596, 1011, 1001,30)
    )

    val customers:Map[Int,Customer] = Map(
      6000 -> new Customer(6000, "CUSTOMER1")
    )
}
