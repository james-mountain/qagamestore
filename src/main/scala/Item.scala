/**
  * Created by Administrator on 19/06/2017.
  */
class Item(i_id : Int, itemName: String, itemStockPrice : Double, sPrice: Double, itemStock : Int, iType : String) {
  val id = i_id
  var name = itemName
  var stockPrice = itemStockPrice
  var salePrice = sPrice
  var stock = itemStock
  val itemtype = iType

  def getID() = id

  def getName() = name
  def setName(newName : String) = name = newName

  def getStockPrice() = stockPrice
  def setStockPrice(newPrice : Double) = stockPrice = newPrice

  def getSalePrice() = salePrice
  def setSalePrice(newPrice : Double) = salePrice = newPrice

  def getStockRemaining() = stock
  def setStockRemaining(newStockValue : Int) = stock = newStockValue

  def getItemType() = itemtype
}
