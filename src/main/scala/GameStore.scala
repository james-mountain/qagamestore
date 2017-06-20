import java.time.LocalDate

import scala.collection.mutable.ListBuffer
import scala.io.Source

/**
  * Created by Administrator on 20/06/2017.
  */
class GameStore {
  var items : ListBuffer[Item] = ListBuffer.empty[Item]
  var employees : ListBuffer[Employee] = ListBuffer.empty[Employee]
  var customers : ListBuffer[Customer] = ListBuffer.empty[Customer]
  //var receipts = List[Receipt]

  def getItems() = items
  def getEmployees() : ListBuffer[Employee] = { employees }
  def getCustomers() : ListBuffer[Customer] = { customers }

  def addItem(item : Item) = items += item
  def addEmployee(employee : Employee) : Unit = { employees += employee }
  def addCustomer(customer : Customer) : Unit = { customers += customer }

  def updateItem(itemid : Int) : Item = {
    items.filter(item => item.getID() == itemid).head
  }

  def deleteItem(itemid : Int) : Boolean = {
    items.find(item => item.getID() == itemid) match {
      case None => false
      case Some(item) => items.remove(items.indexOf(item)); true
    }
  }

  def updateEmployee(employeeid : Int) : Employee = {
    employees.filter(employee => employee.getId() == employeeid).head
  }

  def updateCustomer(customerid : Int) : Customer = {
    customers.filter(customer => customer.getId() == customerid).head
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
}