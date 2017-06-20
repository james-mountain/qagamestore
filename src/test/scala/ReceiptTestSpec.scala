
/**
  * Created by Calin on 20/06/2017.
  */
import org.scalatest._
import java.time.LocalDate
import scala.collection.mutable.ListBuffer
class ReceiptTestSpec extends FlatSpec{
  var r=new Receipt(1,ListBuffer[Double](),0,ListBuffer[String](),"Cash")

  "A receipt " should "have all the right information displayed " in{
    assert(r.getId()==1 &&r.getTotal()==0&&r.getPaymentType()=="Cash")
  }
 // r.addPrice(15)
  r.addItem(new Item(1,"First",10,15,150,"PC"),2)
  "Add a price into the receipt and check size of list " should "be 1 " in{
    assert(r.getPrices().size==1)
  }


  "Add an item to the receipt list" should "have list size equal to 1" in{
    assert(r.getItems().size==1)
  }
  "The to string method" should "display current date" in{
    assert(r.date==LocalDate.now().toString)
  }
}
