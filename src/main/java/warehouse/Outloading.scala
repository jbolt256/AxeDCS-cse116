package warehouse
import warehouse.objects._
import warehouse.Layout
import warehouse.batches._

object Outloading {

  val assignments:Map[Int, Assignment] = Map(
    2000 -> new Assignment(2000, 6000, 529)
  )

  assignments(2000).picklist = Seq(
    (Layout.aisles(596).items(1010), 1), // Pick 1 from 596-1010
    (Layout.aisles(596).items(1011), 2)  // Pick 2 from 596-1011
  )
}
