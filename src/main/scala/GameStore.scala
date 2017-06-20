import scala.collection.mutable.ListBuffer
import scala.io.Source

/**
  * Created by Administrator on 20/06/2017.
  */
class GameStore {
  var items : ListBuffer[Item] = ListBuffer.empty[Item]
  //var employees = List[Employee]
  //var customers = List[Customers]
  //var receipts == List[Receipt]

  def loadItems() = {
    val fileItems = Source.fromFile("C:/Users/Administrator/Downloads/patmat/QAGameStore/src/main/scala/games").getLines().toList
    val fileStrings = fileItems.map(str => str.split(","))

    for (line <- fileStrings) {
      val newItem = new Item(line(0).toInt, line(1), line(2).toDouble, line(3).toDouble, line(4).toInt, line(5))
      addItem(newItem)
    }
  }

  def getItems() = items

  def addItem(item : Item) = items += item
  def updateItem(itemid : Int) = {}
  def deleteItem() = {}
}
