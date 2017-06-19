/**
  * Created by Administrator on 19/06/2017.
  */
class ItemTest {
  val testingitem = new Item(1, "XBOX", 500.00, 500.0, 10, "Console")
  assert(testingitem.getName() == "XBOX", "getName test")
}

object ItemTest {
  def main(args: Array[String]): Unit = {
    new ItemTest
  }
}