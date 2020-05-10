package warehouse.objects

class Product(val number:Int, var name:String, val quantityPallet:Int, val caseWeight:Double) {}
class Pickslot(val aisle:Int, val number:Int, var product:Int, var quantity:Int) {
  override def toString:String = {
    val nString:String = this.number.toString
    nString
  }
}
