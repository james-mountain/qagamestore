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

  val gameStoreThree = new GameStore
  "Game Store" should "be able to update employee information" in {
    val employeeid = 5

    gameStoreThree.updateEmployee(employeeid)
  }

  it should "be able to update customer information also" in {
    val customerid = 7

    gameStoreThree.updateCustomer(customerid)
  }

  val gameStoreFive = new GameStore
  "Game Store" should "be able to load all the previous receipts" in {
    FileHandler.loadFiles(gameStoreFive)

    gameStoreFive.getReceipts().length shouldBe 1
    gameStoreFive.getReceipts().foreach(receipt => println(receipt.toString()))
  }

  it should "be able to add items to a receipt, save it and then reload it" in {
    val newreceipt = gameStoreFive.createNewReceipt()
    FileHandler.loadFiles(gameStoreFive)

    gameStoreFive.addItemToReceipt(newreceipt, gameStoreFive.updateItem(4), 2)
    gameStoreFive.addItemToReceipt(newreceipt, gameStoreFive.updateItem(6), 3)

    gameStoreFive.closeReceipt(newreceipt)

    val gameStoreSix = new GameStore
    FileHandler.loadFiles(gameStoreSix)
    gameStoreSix.getReceipts().foreach(receipt => println(receipt.toString()))
  }
}
