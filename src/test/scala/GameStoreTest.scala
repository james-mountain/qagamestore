/**
  * Created by James Mountain on 19/06/2017.
  */
import java.time.LocalDate

import org.scalatest._

class GameStoreSpec extends FlatSpec with Matchers {
  val newitem = new Item(1, "XBOX", 500.00, 500.0, 10, "Console")
  val newCustomer = new Customer(0,"Peter", "Peter@gmail.com", 5000)

  "Game Store" should "be able to add items for sale" in {
    GameStore.addItem(newitem)
    GameStore.getItems().length shouldBe 1
  }

  it should "be able to remove items from sale" in {
    GameStore.getItems().length shouldBe 1
    GameStore.deleteItem(1)
    GameStore.getItems().length shouldBe 0
  }

  it should "be able to update item information" in {
    val itemid = 1
    val newstockprice = 50.00

    GameStore.addItem(newitem)
    GameStore.getItems().length shouldBe 1
    GameStore.getItemByID(itemid).setStockPrice(newstockprice)
    GameStore.getItems().filter(item => item.getID() == itemid).head.getStockPrice() shouldBe newstockprice
  }

  it should "be able to add a new customer to its records" in {
    GameStore.addCustomer(newCustomer)
    GameStore.getCustomers().length shouldBe 1
  }

  it should "be able to remove a customer from its records" in {
    val previousCustomers : Int = GameStore.getCustomers.length
    GameStore.deleteCustomerByID(0)
    GameStore.getCustomers.length == previousCustomers-1 shouldBe true
  }

  it should "be able to update a customers details" in {
    val customerid = 0
    val newCustomerName = "Jerry"
    GameStore.addCustomer(newCustomer)
    GameStore.getCustomers().length shouldBe 1
    GameStore.getCustomerByID(customerid).setFullName(newCustomerName)
    GameStore.getCustomers().filter(customer => customer.getId() == customerid).head.getFullName() shouldBe newCustomerName
  }
  
  it should "be able load the items from a file to import" in {
    FileHandler.loadFiles()
    GameStore.getItems().length shouldBe 19
  }

  it should "be able load the employees from a file to import" in {
    FileHandler.loadFiles()
    GameStore.getEmployees().length shouldBe 3
  }

  it should "be able to load the customers from a file to import" in {
    FileHandler.loadFiles()
    GameStore.getCustomers().length shouldBe 5
  }
  
  it should "be able to update employee information" in {
    val employeeid = 2

    FileHandler.loadFiles()

    GameStore.getEmployeeByID(employeeid).setIsManager(true)
    GameStore.getEmployees().filter(emply => emply.getId() == employeeid).head.getIsManager() shouldBe true
  }

  it should "be able to delete employees (released/fired)" in {
    val employeeid = 2

    FileHandler.loadFiles()

    val amountOfEmployees = GameStore.getEmployees().length
    GameStore.deleteEmployeeByID(employeeid)
    GameStore.getEmployees().length < amountOfEmployees shouldBe true
  }
  
  it should "be able to load all the previous receipts" in {
    FileHandler.loadFiles()

    GameStore.getReceipts().length should be > 0
    GameStore.getReceipts().foreach(receipt => println(receipt.toString()))
  }

  it should "be able to add items to a receipt, save it and then reload it" in {
    val newreceipt = GameStore.createNewReceipt()
    FileHandler.loadFiles()

    GameStore.addItemToReceipt(newreceipt, GameStore.getItemByID(4), 2)
    GameStore.addItemToReceipt(newreceipt, GameStore.getItemByID(6), 3)

    newreceipt.setPaymentType("card")
    GameStore.closeReceipt(newreceipt, None) shouldBe true

    FileHandler.loadFiles()
    GameStore.getReceipts().foreach(receipt => println(receipt.toString()))
    println("Total profit for 2017-06-20 ="+GameStore.totalProfitForDay("2017-06-20"))
  }

  it should "be able to forecast the profits for a certain period" in {
    val forecast = GameStore.forecastProfits(LocalDate.now(), LocalDate.now().plusDays(12))
    println(forecast)
  }
}
