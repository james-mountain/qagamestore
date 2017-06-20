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

  def getItems() = items
  def addItem(item : Item) = items += item
  def updateItem(itemid : Int) : Item = {
    items.filter(item => item.getID() == itemid).head
  }
  def deleteItem(itemid : Int) : Boolean = {
    items.find(item => item.getID() == itemid) match {
      case None => false
      case Some(item) => items.remove(items.indexOf(item)); true
    }
  }
}
