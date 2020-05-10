package warehouse.objects

// ASSIGNMENTS
class Assignment(val number:Int, val customer:Int, val door:Int) {
  // Picklist is everything to be picked, with quantity
  var picklist:Seq[(Pickslot,Int)] = Seq()

  // Weight is total weight of assignment, in pounds
  var weight:Double = 0.0
}
