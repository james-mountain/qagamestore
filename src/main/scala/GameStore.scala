import scala.collection.mutable.ListBuffer

/**
  * Created by Administrator on 20/06/2017.
  */
object GameStore {
  var items : ListBuffer[Item] = ListBuffer.empty[Item]
  var employees : ListBuffer[Employee] = ListBuffer.empty[Employee]
  var customers : ListBuffer[Customer] = ListBuffer.empty[Customer]
  var receipts : ListBuffer[Receipt] = ListBuffer.empty[Receipt]

  def getItems() = items
  def getEmployees() : ListBuffer[Employee] = { employees }
  def getCustomers() : ListBuffer[Customer] = { customers }
  def getReceipts() : ListBuffer[Receipt] = { receipts }

  def addItem(item : Item) = items += item
  def addReceipt(receipt : Receipt) = receipts += receipt
  def addEmployee(employee : Employee) = employees += employee
  def addCustomer(customer : Customer) = customers += customer

  def getItemByID(itemid : Int) : Item = {
    items.filter(item => item.getID() == itemid).head
  }

  def getEmployeeByID(employeeid : Int) : Employee = {
    employees.filter(employee => employee.getId() == employeeid).head
  }

  def getCustomerByID(customerid : Int) : Customer = {
    customers.filter(customer => customer.getId() == customerid).head
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

  def addItemToReceipt(receipt: Receipt, item : Item, quantity : Int) = {
    if (quantity <= item.getStockRemaining()) {
      receipt.addItem(item, quantity)
      item.setStockRemaining(item.getStockRemaining() - quantity)
    }
  }

  def closeReceipt(receipt : Receipt) : Boolean = receipt.getPaymentType() match {
    case "cash" | "card" => {
      receipts += receipt
      FileHandler.saveItems()
      FileHandler.saveReceipts()
      true
    }
    case _ => false
  }

  def totalProfitForDay(date:String):Double={
    var sum:Double=0
    receipts.filter(p=>p.date==date).foreach(r=>sum+=r.getTotal())
    sum

  }

  def dailyReceiptsByDate(date:String)={
    receipts.filter(rep=>rep.date==date).foreach(println)
  }
}