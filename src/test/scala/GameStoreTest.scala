/**
  * Created by James Mountain on 19/06/2017.
  */
import org.scalatest._

class GameStoreSpec extends FlatSpec with Matchers {
  val gamestore = new GameStore
  val newitem = new Item(1, "XBOX", 500.00, 500.0, 10, "Console")
  val newCustomer = new Customer(0,"Peter", "Peter@gmail.com", 5000)

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
    gamestore.getItems().filter(item => item.getID() == itemid).head.getStockPrice() shouldBe newstockprice
  }

  it should "be able to add a new customer to its records" in {
    gamestore.addCustomer(newCustomer)
    gamestore.getCustomers().length shouldBe 1
  }

  it should "be able to remove a customer from its records" in {
    gamestore.getCustomers.length shouldBe 1
    gamestore.deleteCustomerByID(0)
    gamestore.getCustomers.length shouldBe 0
  }

  it should "be able to update a customers details" in {
    val customerid = 0
    val newCustomerName = "Jerry"
    gamestore.addCustomer(newCustomer)
    gamestore.getCustomers().length shouldBe 1
    gamestore.updateCustomer(customerid).setFullName(newCustomerName)
    gamestore.getCustomers().filter(customer => customer.getId() == customerid).head.getFullName() shouldBe newCustomerName
  }

  val anothergamestore = new GameStore
  "Another Game Store" should "be able load the items from a file to import" in {
    FileHandler.loadFiles(anothergamestore)
    anothergamestore.getItems().length shouldBe 19
  }

  it should "be able load the employees from a file to import" in {
    FileHandler.loadFiles(anothergamestore)
    anothergamestore.getEmployees().length shouldBe 3
  }

  it should "be able to load the customers from a file to import" in {
    FileHandler.loadFiles(anothergamestore)
    anothergamestore.getCustomers().length shouldBe 5
  }

  val gameStoreThree = new GameStore
  "Game Store" should "be able to update employee information" in {
    val employeeid = 2

    FileHandler.loadFiles(gameStoreThree)

    gameStoreThree.updateEmployee(employeeid).setIsManager(true)
    gameStoreThree.getEmployees().filter(emply => emply.getId() == employeeid).head.getIsManager() shouldBe true
  }

  it should "be able to delete employees (released/fired)" in {
    val employeeid = 2

    FileHandler.loadFiles(gameStoreThree)

    val amountOfEmployees = gameStoreThree.getEmployees().length
    gameStoreThree.deleteEmployeeByID(employeeid)
    gameStoreThree.getEmployees().length < amountOfEmployees shouldBe true
  }

  val gameStoreFive = new GameStore
  "Game Store" should "be able to load all the previous receipts" in {
    FileHandler.loadFiles(gameStoreFive)

    gameStoreFive.getReceipts().length should be > 0
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
    println("Total profit for 2017-06-20 ="+gameStoreSix.totalProfitForDay("2017-06-20"))
    //gameStoreSix.dailyReceiptsByDate("2017-06-20")
  }
}
