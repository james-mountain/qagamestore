import java.io.{File, PrintWriter}
import java.time.LocalDate

import scala.collection.mutable.ListBuffer
import scala.io.Source

/**
  * Created by Administrator on 20/06/2017.
  */
object FileHandler {
  val prefixfilename = "C:/Users/Administrator/Desktop/GAMESTORE/"

  def loadFiles(gameStore : GameStore): Unit = {
    loadItems(gameStore)
    loadCustomer(gameStore)
    loadEmployee(gameStore)
    loadReceipts(gameStore)
  }

  def loadItems(gameStore : GameStore) = {
    val gameFileItems = Source.fromFile(prefixfilename + "games.txt").getLines().toList
    val nonGameItems = Source.fromFile(prefixfilename + "items.txt").getLines().toList
    val gameFileStrings = gameFileItems.map(str => str.split(","))
    val nonGamefileStrings = nonGameItems.map(str => str.split(","))

    // game items
    for (line <- gameFileStrings) {
      val newItem = new Game(line(0).toInt, line(1), line(2).toDouble, line(3).toDouble, line(4).toInt, line(5), LocalDate.parse(line(6)), line(7))
      gameStore.addItem(newItem)
    }

    // non game items
    for (line <- nonGamefileStrings) {
      val newItem = new Item(line(0).toInt, line(1), line(2).toDouble, line(3).toDouble, line(4).toInt, line(5))
      gameStore.addItem(newItem)
    }
  }

  def loadCustomer(gameStore : GameStore) = {
    val customerRecords = Source.fromFile("C:\\Users\\Administrator\\Desktop\\Customers").getLines().toList
    val customerStrings = customerRecords.map(str => str.split(","))

    for(line <- customerStrings) {
      val newCustomer = new Customer(line(0).toInt, line(1), line(2), line(3).toInt)
      println(line(4))
      val preOrderString = line(4).split('|').toList
      for (order <- preOrderString) {
        newCustomer.addPreOrder(order.toInt)
      }
    }   
  }
  
  def loadReceipts(gameStore : GameStore) = {
    val receiptItems = Source.fromFile(prefixfilename + "receipts.txt").getLines().toList
    val receiptStrings = receiptItems.map(str => str.split(";"))

    gameStore.receipts = ListBuffer.empty[Receipt]

    // game items
    for (line <- receiptStrings) {
      val newItem = new Receipt(line(0).toInt, line(1).split(",").map(string => string.toDouble).toList, line(2).toDouble, line(3).split(",").toList, line(4), line(5))
      gameStore.addReceipt(newItem)
    }
  }

  def saveFiles(gameStore : GameStore) = {
    saveItems(gameStore)
    saveCustomer(gameStore)
    saveEmployee(gameStore.employees)
  }

  def saveItems(gameStore : GameStore): Unit = {
    val games = ListBuffer.empty[Game]
    val items = ListBuffer.empty[Item]

    gameStore.getItems().foreach {
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

  def saveCustomer(gameStore : GameStore) : Unit = {
    val customers = ListBuffer.empty[Customer]

    gameStore.getCustomers().foreach {
      case customer: Customer => customers += customer
    }
    
    val customerWriter = new PrintWriter(new File("C:\\Users\\Administrator\\Desktop\\customers"))
    customers.foreach(customer => customerWriter.println(customer.toString))
    customerWriter.close()
  }
  
  def saveEmployee(employees: ListBuffer[Employee]): Unit = {
    val employeeWriter = new PrintWriter(new File(prefixfilename + "employee.txt"))
    employees.foreach(employee => employeeWriter.println(employee.toString))
    employeeWriter.close()
  }

  def loadEmployee(gameStore : GameStore): Unit = {
    val employees = Source.fromFile(prefixfilename + "employee.txt").getLines().toList
    val employeeStrings = employees.map(str => str.split(","))

    gameStore.employees = ListBuffer.empty[Employee]

    for (line <- employeeStrings) {
      //id, Name, Email, isManager, Address. Tel, Passport
      val employee = new Employee(line(0).toInt, line(1), line(2), line(3).toBoolean, line(4), line(5), line(6))
      gameStore.addEmployee(employee)
    }
  }
    
  def saveReceipts(gameStore : GameStore) = {
    val receiptWriter = new PrintWriter(new File(prefixfilename + "receipts.txt"))
    gameStore.getReceipts().map(receipt => receipt.toWritableString()).foreach(receipt => receiptWriter.println(receipt.toString))
    receiptWriter.close()
  }
}
