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
    gamestore.getItems().filter(item => item.getID() == itemid).head.getStockPrice() == newstockprice
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
    anothergamestore.getItems.length shouldBe 19
  }

  it should "be able to load the customers from a file to import" in {
    anothergamestore.getCustomers().length shouldBe 5
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
}
