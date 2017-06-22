import java.io.{File, PrintWriter}
import java.time.LocalDate

import scala.collection.mutable.ListBuffer
import scala.io.Source

/**
  * Created by Administrator on 20/06/2017.
  */
object FileHandler {
  val prefixfilename = "C:/Users/Administrator/Desktop/GAMESTORE/"

  def loadFiles(): Unit = {
    loadItems()
    loadCustomer()
    loadEmployee()
    loadReceipts()
  }

  def loadItems() = {
    val gameFileItems = Source.fromFile(prefixfilename + "games.txt").getLines().toList
    val nonGameItems = Source.fromFile(prefixfilename + "items.txt").getLines().toList
    val gameFileStrings = gameFileItems.map(str => str.split(","))
    val nonGamefileStrings = nonGameItems.map(str => str.split(","))

    GameStore.items = ListBuffer.empty[Item]

    // game items
    for (line <- gameFileStrings) {
      val newItem = new Game(line(0).toInt, line(1), line(2).toDouble, line(3).toDouble, line(4).toInt, line(5), LocalDate.parse(line(6)), line(7))
      GameStore.addItem(newItem)
    }

    // non game items
    for (line <- nonGamefileStrings) {
      val newItem = new Item(line(0).toInt, line(1), line(2).toDouble, line(3).toDouble, line(4).toInt, line(5))
      GameStore.addItem(newItem)
    }
  }

  def loadCustomer() = {
    val customerRecords = Source.fromFile(prefixfilename + "customers.txt").getLines().toList
    val customerStrings = customerRecords.map(str => str.split(","))

    GameStore.customers = ListBuffer.empty[Customer]

    for(line <- customerStrings) {
      val newCustomer = new Customer(line(0).toInt, line(1), line(2), line(3).toInt)
      if (line.length == 5) {
        val preOrderString = line(4).split('|').toList
        for (order <- preOrderString) {
          newCustomer.addPreOrder(order.toInt)
        }
      }
      GameStore.addCustomer(newCustomer)
    }   
  }
  
  def loadReceipts() = {
    val receiptItems = Source.fromFile(prefixfilename + "receipts.txt").getLines().toList
    val receiptStrings = receiptItems.map(str => str.split(";"))

    GameStore.receipts = ListBuffer.empty[Receipt]

    // game items
    for (line <- receiptStrings) {
      if (line.length == 6) {
        val newItem = new Receipt(line(0).toInt, line(1).split(",").map(string => string.toDouble).toList, line(2).toDouble, line(3).split(",").toList, line(4), line(5))
        GameStore.addReceipt(newItem)
      }
    }
  }

  def saveFiles() = {
    saveItems()
    saveCustomer()
    saveEmployee()
    saveReceipts()
  }

  def saveItems(): Unit = {
    val games = ListBuffer.empty[Game]
    val items = ListBuffer.empty[Item]

    GameStore.getItems().foreach {
      case item: Game => games += item
      case item: Item => items += item
    }
    
    val gamesWriter = new PrintWriter(new File(prefixfilename + "games.txt"))
    games.foreach(game => gamesWriter.println(game.toString))
    gamesWriter.close()

    val itemsWriter = new PrintWriter(new File(prefixfilename + "items.txt"))
    items.foreach(item => itemsWriter.println(item.toString))
    itemsWriter.close()
  }

  def saveCustomer() : Unit = {
    val customerWriter = new PrintWriter(new File(prefixfilename + "customers.txt"))
    GameStore.getCustomers().foreach(customer => customerWriter.println(customer.toString))
    customerWriter.close()
  }
  
  def saveEmployee(): Unit = {
    val employeeWriter = new PrintWriter(new File(prefixfilename + "employee.txt"))
    GameStore.getEmployees().foreach(employee => employeeWriter.println(employee.toString))
    employeeWriter.close()
  }

  def loadEmployee(): Unit = {
    val employees = Source.fromFile(prefixfilename + "employee.txt").getLines().toList
    val employeeStrings = employees.map(str => str.split(","))

    GameStore.employees = ListBuffer.empty[Employee]

    for (line <- employeeStrings) {
      //id, Name, Email, isManager, Address. Tel, Passport
      val employee = new Employee(line(0).toInt, line(1), line(2), line(3).toBoolean, line(4), line(5), line(6))
      GameStore.addEmployee(employee)
    }
  }
    
  def saveReceipts() = {
    val receiptWriter = new PrintWriter(new File(prefixfilename + "receipts.txt"))
    GameStore.getReceipts().map(receipt => receipt.toWritableString()).foreach(receipt => receiptWriter.println(receipt.toString))
    receiptWriter.close()
  }
}
