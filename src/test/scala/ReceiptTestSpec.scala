
/**
  * Created by Calin on 20/06/2017.
  */
import org.scalatest._

import scala.collection.mutable.ListBuffer
class ReceiptTestSpec extends FlatSpec{
  var r=new Receipt(1,ListBuffer[Double](),0,ListBuffer[String](),"Cash")

  "A receipt " should "have all the right information displayed " in{
    assert(r.getId()==1 &&r.getTotal()==0&&r.getPaymentType()=="Cash")
  }
  r.addPrice(15)

  "Add a price into the receipt and check size of list " should "be 1 " in{
    assert(r.getPrices().size==1)
  }
   r.addItem("Call of Duty",2)

  "Add an item to the receipt list" should "have list size equal to 1" in{
    assert(r.getItems().size==1)
  }
}
