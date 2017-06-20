import java.time.LocalDate

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
    val gameFileItems = Source.fromFile("C:/Users/Administrator/Desktop/GAMESTORE/games.txt").getLines().toList
    val nonGameItems = Source.fromFile("C:/Users/Administrator/Desktop/GAMESTORE/items.txt").getLines().toList
    val gameFileStrings = gameFileItems.map(str => str.split(","))
    val nonGamefileStrings = nonGameItems.map(str => str.split(","))

    // game items
    for (line <- gameFileStrings) {
      val newItem = new Game(line(0).toInt, line(1), line(2).toDouble, line(3).toDouble, line(4).toInt, line(5), LocalDate.parse(line(6)), line(7))
      addItem(newItem)
    }

    // non game items
    for (line <- nonGamefileStrings) {
      val newItem = new Item(line(0).toInt, line(1), line(2).toDouble, line(3).toDouble, line(4).toInt, line(5))
      addItem(newItem)
    }
  }


  def getItems() = items
  def addItem(item : Item) = items += item
  def updateItem(itemid : Int) = {}
  def deleteItem() = {}
}
