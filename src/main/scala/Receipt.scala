import scala.collection.mutable.ListBuffer
import java.time.LocalDate
/**
  * Created by Calin on 19/06/2017.
  */
class Receipt(protected val id:Int, protected var prices:ListBuffer[Double],protected var total:Double,protected var items:ListBuffer[String],protected var paymentType:String){
  val date=LocalDate.now().toString
  def getId()={id}
  def getPrices()={prices.toList}
  def addPrice(price:Double)={prices+=price}
  def getTotal()={total}
  def getItems()={items}
  def getPaymentType()={paymentType}
  def addItem(item:Item,quantity:Int)={
    val subprice=quantity*item.salePrice
    items+=item.getName()+", Quantity="+quantity.toString+" £"+subprice.toString
    total+=subprice
    addPrice(subprice)
  }

  override def toString()={
    var rows=""
    items.foreach(item=>rows=rows+"\n"+item)
    id+"  "+"Date: "+date+"\n"+"Items:"+rows

  }


}
