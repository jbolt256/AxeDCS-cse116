package warehouse.batches
import warehouse.objects._
import warehouse.Layout
// LOADING SCHEDULES

object Schedule {

    // Convert assignment to readable bill
    def formAssignment(bill:Assignment):String = {
      var out:String = "BEGIN ASSIGNMENT " + bill.number + "; CUSTOMER " + bill.customer + "(" + Layout.customers(bill.customer).name + ")<br/>"
      var weight:Double = 0.0
      var i:Int = 0
      for ( item <- bill.picklist ) {
        out = out + "PICK SLOT " + item._1.aisle.toString + "-" + item._1.toString + "; QUANTITY " + bill.picklist(i)._2 + "; ITEM " + Layout.products(item._1.product).name + "<br/>"
        weight += Layout.products(item._1.product).caseWeight * bill.picklist(i)._2
        i += 1
      }
      out += "END ASSIGNMENT; TOTAL WEIGHT " + weight + "LBS\r\n"
      bill.weight = weight
      out
    }

    def runAssignment(bill:Assignment):String = {
      var i:Int = 0
      for ( item <- bill.picklist ) {
        Layout.aisles(bill.picklist(i)._1.aisle).items(bill.picklist(i)._1.number).quantity -= bill.picklist(i)._2
      }
      "RAN ASSIGN"
    }
}
