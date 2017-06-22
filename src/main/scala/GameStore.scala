import java.time.LocalDate
import java.time.temporal.ChronoUnit

import scala.collection.mutable.ListBuffer

/**
  * Created by Administrator on 20/06/2017.
  */
object GameStore {
  var items : ListBuffer[Item] = ListBuffer.empty[Item]
  var employees : ListBuffer[Employee] = ListBuffer.empty[Employee]
  var customers : ListBuffer[Customer] = ListBuffer.empty[Customer]
  var receipts : ListBuffer[Receipt] = ListBuffer.empty[Receipt]

  FileHandler.loadFiles()

  def getItems() = items
  def getEmployees() : ListBuffer[Employee] = { employees }
  def getCustomers() : ListBuffer[Customer] = { customers }
  def getReceipts() : ListBuffer[Receipt] = { receipts }

  def addItem(item : Item) = items += item
  def addReceipt(receipt : Receipt) = receipts += receipt
  def addEmployee(employee : Employee) = employees += employee
  def addCustomer(customer : Customer) = customers += customer

  def registerCustomer(fullName : String, email : String) : Customer = {
    var counter = customers.lastOption match {
      case Some(customer) => customer.getId()
      case None => 0
    }
    val newcustomer = new Customer(counter+1, fullName, email, 0)
    addCustomer(newcustomer)
    newcustomer
  }

  def registerEmployee(fullName : String, email : String, ismanager : Boolean, address : String, tel : String, pass : String) : Employee = {
    var counter = employees.lastOption match {
      case Some(employee) => employee.getId()
      case None => 0
    }
    val newEmployee = new Employee(counter+1, fullName, email, ismanager, address, tel, pass)
    addEmployee(newEmployee)
    newEmployee
  }

  def registerItem(name : String, stockPrice : Double, salePrice : Double, stock: Int, iType : String, rDate : LocalDate, rating: String): Game = {
    val counter = items.maxBy(item => item.getID()).getID()
    val newGame = new Game(counter+1, name, stockPrice, salePrice, stock, iType, rDate, rating)
    addItem(newGame)
    newGame
  }

  def registerItem(name : String, stockPrice : Double, salePrice : Double, stock: Int, iType : String) : Item = {
    val counter = items.maxBy(item => item.getID()).getID()
    val newItem = new Item(counter+1, name, stockPrice, salePrice, stock, iType)
    addItem(newItem)
    newItem
  }

  def getItemByID(itemid : Int) : Item = {
    items.filter(item => item.getID() == itemid).head
  }

  def getEmployeeByID(employeeid : Int) : Employee = {
    employees.filter(employee => employee.getId() == employeeid).head
  }

  def getCustomerByID(customerid : Int) : Customer = {
    customers.filter(customer => customer.getId() == customerid).head
  }

  def getCustomerByEmail(customermail : String) : Option[Customer] = {
    customers.find(customer => customer.getEmail() == customermail)
  }

  def deleteItem(itemid : Int) : Boolean = {
    items.find(item => item.getID() == itemid) match {
      case None => false
      case Some(item) => items.remove(items.indexOf(item)); true
    }
  }

  def deleteEmployeeByID(employeeid : Int) : Boolean = {
    employees.find(employee => employee.getId() == employeeid) match {
      case None => false
      case Some(emp) => employees.remove(employees.indexOf(emp)); true
    }
  }

  def deleteCustomerByID(customerid : Int) : Boolean = {
    customers.find(customer => customer.getId() == customerid) match {
      case None => false
      case Some(cust) => customers.remove(customers.indexOf(cust)); true
    }
  }
  def createNewReceipt(): Receipt = {
    var counter = receipts.lastOption match {
      case Some(receipt) => receipt.getId()
      case None => 0
    }
    new Receipt(counter+1)
  }

  def addItemToReceipt(receipt: Receipt, item : Item, quantity : Int) : Boolean = {
    if (quantity <= item.getStockRemaining()) {
      receipt.addItem(item, quantity)
      item.setStockRemaining(item.getStockRemaining() - quantity)
      true
    } else false
  }

  def closeReceipt(receipt : Receipt, customer : Option[Customer]) : Boolean = {
    if (receipt.getItems().length == 0) false
    else receipt.getPaymentType() match {
      case "cash" | "card" => {
        receipts += receipt
        customer match {
          case Some(cust) => cust.addMembershipPoints(cust.convertMoneyToPoints(receipt.getTotal()))
          case _ => {}
        }

        FileHandler.saveItems()
        FileHandler.saveReceipts()
        FileHandler.saveCustomer()
        true
      }
      case _ => false
    }
  }

  def applyDiscount(receipt : Receipt, customer : Customer, points : Int) = {
    if (customer.convertPointsToMoney(points) <= receipt.getTotal() && customer.getMembershipPoints() >= points) {
      receipt.setTotal(receipt.getTotal() - customer.convertPointsToMoney(points))
      customer.removeMembershipPoints(points)
    }
  }

  def totalProfitForDay(date:String) : Double = {
    var sum:Double=0
    receipts.filter(p=>p.date==date).foreach(r=>sum+=r.getTotal())
    Math.round(sum*100.0)/100.0
  }

  def dailyReceiptsByDate(date:String) : ListBuffer[Receipt] ={
    receipts.filter(rep=>rep.date==date)
  }

  def checkForUser(emailField: String, passwordField: String): Employee ={
    def iter(i: Int): Employee = i match {
      case a if i == employees.length => null
      case b if employees(i).getPass() == passwordField && employees(i).getEmail() == emailField => employees(i)
      case _ => iter(i+1)
    }
    iter(0)
  }

  def forecastProfits(startDate: LocalDate, endDate: LocalDate): Double = {
    var total : Double = 0.0
    val daysToAnalyze = 7

    receipts.foreach(receipt => {
      val date : LocalDate = LocalDate.parse(receipt.date)
      if(date.isAfter(LocalDate.now().minusDays(daysToAnalyze+1)) && date.isBefore(LocalDate.now()))
      {
        total += receipt.getTotal()
      }
    })

    val averagePerDay = total/daysToAnalyze.toDouble
    var days : Long = 0
    if(startDate.isBefore(endDate))
    {
      days = startDate.until(endDate, ChronoUnit.DAYS) + 1
    }

    return Math.round(averagePerDay*days*100.0)/100.0
  }
}