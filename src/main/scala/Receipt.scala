import scala.collection.mutable.ListBuffer

/**
  * Created by Calin on 19/06/2017.
  */
class Receipt(protected val id:Int, protected var prices:ListBuffer[Double],protected var total:Double,protected var items:ListBuffer[String],protected var paymentType:String) {

  def getId()={id}
  def getPrices()={prices.toList}
  def addPrice(price:Double)={prices+=price}
  def getTotal()={total}
  def getItems()={items}
  def getPaymentType()={paymentType}
  def addItem(item:String,quantity:Int)={items+=item+", Quantity= "+quantity.toString}


}
