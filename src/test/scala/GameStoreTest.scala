/**
  * Created by James Mountain on 19/06/2017.
  */
import org.scalatest._

class GameStoreSpec extends FlatSpec with Matchers {
  val gamestore = new GameStore
  val newitem = new Item(1, "XBOX", 500.00, 500.0, 10, "Console")

  "Game Store" should "be able to add items for sale" in {
    gamestore.addItem(newitem)
    gamestore.getItems().length shouldBe 1
  }

  it should "be able to remove items from sale" in {
    gamestore.getItems().length shouldBe 1
    gamestore.deleteItem(1)
    gamestore.getItems().length shouldBe 0
  }

  it should "be able to update item information" in {
    val itemid = 1
    val newstockprice = 50.00

    gamestore.addItem(newitem)
    gamestore.getItems().length shouldBe 1
    gamestore.updateItem(itemid).setStockPrice(newstockprice)
    gamestore.getItems().filter(item => item.getID() == itemid).head.getStockPrice() == newstockprice
  }

  val anothergamestore = new GameStore
  "Another Game Store" should "be able load the items from a file to import" in {
    FileHandler.loadFiles(anothergamestore)
    anothergamestore.getItems.length shouldBe 19
  }
}
