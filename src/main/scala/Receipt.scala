import scala.collection.mutable.ListBuffer

/**
  * Created by Calin on 19/06/2017.
  */
class Receipt(protected val id:Int, protected var prices:ListBuffer[Double],protected var total:Double,protected var items:List[String],protected var paymentType:String) {

  def getId()={}
  def getPrices()={}
  def addPrice(price:Double)={}
  def getTotal()={}
  def getItems()={}
  def addItem(item:String,quantity:Int)={}


}
