/**
  * Created by Administrator on 19/06/2017.
  */

import java.time.LocalDate
import org.scalatest._

class ItemTestSpec extends FlatSpec with Matchers {
  val testingitem = new Item(1, "XBOX", 500.00, 500.0, 10, "Console")
  "XBOX" should "have essential information accessible" in {
    testingitem.getName() shouldBe "XBOX"
    testingitem.getID() shouldBe 1
    testingitem.getItemType() shouldBe "Console"
    testingitem.getSalePrice() shouldBe 500.00
    testingitem.getStockPrice() shouldBe 500.00
    testingitem.getStockRemaining() shouldBe 10
  }

  it should "have relevant information allowed to be modified" in {
    val newname = "XBOX Classic Edition"
    val newsaleprice = 450.00
    val newstockprice = 475.00
    val newstock = 8

    testingitem.setName(newname)
    testingitem.setSalePrice(newsaleprice)
    testingitem.setStockPrice(newstockprice)
    testingitem.setStockRemaining(newstock)

    testingitem.getName() shouldBe newname
    testingitem.getSalePrice() shouldBe newsaleprice
    testingitem.getStockPrice() shouldBe newstockprice
    testingitem.getStockRemaining() shouldBe newstock
  }

  "Shooter Game 22" should "have essential information accessible" in {
    val testinggame = new Game(2, "Shooter Game 22", 50.00, 50.0, 10, "PC", LocalDate.parse("2015-05-20"), "18")

    testinggame.getReleaseDate().getYear shouldBe 2015
    testinggame.getReleaseDate().getMonth shouldBe java.time.Month.MAY
    testinggame.getReleaseDate().getDayOfMonth shouldBe 20
    testinggame.getReleased() shouldBe true

    testinggame.getRating() shouldBe "18"
  }
}