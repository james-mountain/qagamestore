/**
  * Created by James Mountain on 19/06/2017.
  */
import org.scalatest._

class GameStoreSpec extends FlatSpec with Matchers {
  val gamestore = new GameStore

  "Game Store" should "be able to add items for sale" in {
    gamestore.addItem()
    gamestore.getItems().length shouldBe 1
  }

  it should "be able to remove items from sale" in {
    gamestore.getItems().length shouldBe 1
    gamestore.deleteItem()
    gamestore.getItems().length shouldBe 0
  }

  it should "be able to update item information" in {
    val itemid = 5

    gamestore.addItem()
    gamestore.getItems().length shouldBe 1
    gamestore.updateItem(5)
    gamestore.getItems().filter(item => item.getID() == itemid).head.getStockPrice() == 50.00
  }

  val anothergamestore = new GameStore
  "Another Game Store" should "be able load the items from a file to import" in {
    anothergamestore.loadItems()
    anothergamestore.getItems.length shouldBe 11
  }
}
