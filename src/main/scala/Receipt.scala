import scala.collection.mutable.ListBuffer
import java.time.LocalDate
/**
  * Created by Calin on 19/06/2017.
  */
class Receipt(protected val id:Int){
  var date=LocalDate.now().toString
  protected var prices:ListBuffer[Double]=new ListBuffer[Double]
  protected var total:Double=0
  protected var items:ListBuffer[String]=new ListBuffer[String]
  protected var paymentType:String=""
  def getId()={id}
  def getPrices()={prices.toList}
  def addPrice(price:Double)={prices+=price}
  def getTotal()={Math.round(total*100.0)/100.0}
  def getItems()={items}
  def getPaymentType()={paymentType}
  def setPaymentType(pType:String)={paymentType=pType}
  def addItem(item:Item,quantity:Int)={
    val subprice=quantity*item.salePrice
    items+=item.getName()+" - Quantity = "+quantity.toString+" £"+subprice.toString
    total+=subprice
    addPrice(subprice)
  }

  def this(id : Int, prices : List[Double], total : Double, items : List[String], paymentType : String, date : String) {
    this(id)
    this.prices = ListBuffer.empty ++= prices
    this.total = total
    this.items = ListBuffer.empty ++= items
    this.paymentType = paymentType
    this.date = date
  }

  def toWritableString() = {
    List(id.toString, prices.mkString(","), total.toString, items.mkString(","), paymentType, date).mkString(";")
  }

  override def toString()={
    var rows=""
    items.foreach(item=>rows=rows+"\n"+item)
    id+"  "+"Date: "+date+"\n"+"Items:"+rows+"\n\n"+"Total Price: £"+getTotal()
  }
}
