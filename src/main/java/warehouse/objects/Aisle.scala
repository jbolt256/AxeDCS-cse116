package warehouse.objects

class Aisle(val number:Int, val bays:Int) {
    // The slot number is an integer,
    // 1011 - 10-11
    // 5210 - 52-10 and so on...
    // TYPES OF SLOTS:
    // 0 - Pick one slot (10-11)
    // 1 - Pick two slot (10-11, 10-12)
    // 2 - Pick four slot (10-11, 10-12, 10-21, 10-22)
    // 3 - Pick eight slot (10-11, 10-12, 10-21, ..., 10-26)
    // Items maps each slot to an item code
    var items:Map[Int, Pickslot] = Map()

    // Quantity maps each slot to a quantity of product that should be there
    var quantity:Map[Int, Int] = Map()
}
