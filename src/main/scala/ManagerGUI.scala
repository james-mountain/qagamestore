import java.awt.Dimension
import java.time.LocalDate

import scala.collection.mutable.ListBuffer
import scala.swing.ListView.Renderer
import scala.swing.{BoxPanel, Button, CheckBox, ComboBox, Component, Dialog, FlowPanel, GridPanel, Label, ListView, MainFrame, Orientation, PasswordField, ScrollPane, Separator, TextArea, TextField}
import scala.swing.Swing.HStrut
import scala.util.Try

/**
  * Created by Administrator on 22/06/2017.
  */

class ReceiptsPanel(managerGUI: ManagerGUI, receipts : ListBuffer[Receipt]) extends MainFrame {
  preferredSize = new Dimension(700, 350)

  contents = new BoxPanel(Orientation.Vertical) {
    contents += new FlowPanel(new ScrollPane(new ListView(receipts) {
      renderer = Renderer(_.toString())
    }))
    contents += Button("Close") {close()}
  }
}

class ManagerGUI extends MainFrame {
  var loggedEmployee = new Employee(2, "Simon", "simon@hotmail.co.uk", true, "3434 House Street", "01234 562452", "password")
  var itemsComboBox = new ComboBox(GameStore.getItems().map(item => item.getID() + " | " + item.getName()))
  var employeesComboBox = new ComboBox(GameStore.getEmployees().map(emp => emp.getId() + " | " + emp.getFullName()))
  var customersComboBox = new ComboBox(GameStore.getCustomers().map(cust => cust.getId() + " | " + cust.getFullName()))

  var updateMode = false

  title = "Manager Operations -------> Logged in as: " + loggedEmployee.getFullName()
  var curPanelContents : Component = new TextArea("dasdasdas")
  var currentPanel = new BoxPanel(Orientation.Vertical) {
    contents += curPanelContents
  }
  def registerEmployee = new BoxPanel(Orientation.Vertical) {
    val selemploy = GameStore.getEmployeeByID(employeesComboBox.selection.item.split('|').head.trim.toInt)

    val fullnamefield = new TextField() {if (updateMode) text = selemploy.getFullName()}
    val emailfield = new TextField() {if (updateMode) text = selemploy.getEmail()}
    val ismanagercheck = new CheckBox() {if (updateMode) selected = selemploy.getIsManager()}
    val addressfield = new TextField() {if (updateMode) text = selemploy.getAddress()}
    val telfield = new TextField() {if (updateMode) text = selemploy.getTel()}
    val passfield = new PasswordField(if (updateMode) selemploy.getPass() else "") {}

    contents += new GridPanel(6, 2) {
      contents += new Label("Full Name: ")
      contents += fullnamefield
      contents += new Label("Email: ")
      contents += emailfield
      contents += new Label("Is Manager: ")
      contents += ismanagercheck
      contents += new Label("Address: ")
      contents += addressfield
      contents += new Label("Telephone: ")
      contents += telfield
      contents += new Label("Password: ")
      contents += passfield
    }

    if (updateMode) contents += Button("Update Employee") {
      if (fullnamefield.text != "" && emailfield.text != "" && addressfield.text != "" && telfield.text != "" && passfield.password.mkString != "") {
        selemploy.setFullName(fullnamefield.text)
        selemploy.setEmail(emailfield.text)
        selemploy.setIsManager(ismanagercheck.selected)
        selemploy.setAddress(addressfield.text)
        selemploy.setTel(telfield.text)
        selemploy.setPass(passfield.password.mkString)
        Dialog.showMessage(contents.head, "Updated " + fullnamefield.text + "'s details.", "Employee updated")
        employeesComboBox.peer.setModel(new ComboBox(GameStore.getEmployees().map(emp => emp.getId() + " | " + emp.getFullName())).peer.getModel)

        FileHandler.saveEmployee()
        replacePanel(new TextArea("dasdasdas"))
      }
    }
    else contents += Button("Register") {
      if (fullnamefield.text != "" && emailfield.text != "" && addressfield.text != "" && telfield.text != "" && passfield.password.mkString != "") {
        GameStore.registerEmployee(fullnamefield.text, emailfield.text, ismanagercheck.selected, addressfield.text, telfield.text, passfield.password.mkString)
        Dialog.showMessage(contents.head, "Registered " + fullnamefield.text + " to the list of employees.", "Employee registered")
        employeesComboBox.peer.setModel(new ComboBox(GameStore.getEmployees().map(emp => emp.getId() + " | " + emp.getFullName())).peer.getModel)

        FileHandler.saveEmployee()
        replacePanel(new TextArea("dasdasdas"))
      } else Dialog.showMessage(contents.head, "Invalid credentials, please ensure all fields have values", "Invalid credentials")
    }
  }
  def registerCustomer = new BoxPanel(Orientation.Vertical) {
    val selcustomer = GameStore.getCustomerByID(customersComboBox.selection.item.split('|').head.trim.toInt)

    val fullnamefield = new TextField() {if (updateMode) text = selcustomer.getFullName()}
    val emailfield = new TextField() {if (updateMode) text = selcustomer.getEmail()}

    contents += new GridPanel(6, 2) {
      contents += new Label("Full Name: ")
      contents += fullnamefield
      contents += new Label("Email: ")
      contents += emailfield
    }

    if (updateMode) contents += Button("Update Customer") {
      if (fullnamefield.text != "" && emailfield.text != "") {
        selcustomer.setFullName(fullnamefield.text)
        selcustomer.setEmail(emailfield.text)
        Dialog.showMessage(contents.head, "Updated " + fullnamefield.text + "'s details.", "Customer updated")
        customersComboBox.peer.setModel(new ComboBox(GameStore.getCustomers().map(cust => cust.getId() + " | " + cust.getFullName())).peer.getModel)

        FileHandler.saveCustomer()
        replacePanel(new TextArea("dasdasdas"))
      }
    }
    else contents += Button("Register") {
      if (fullnamefield.text != "" && emailfield.text != "") {
        GameStore.registerCustomer(fullnamefield.text, emailfield.text)
        Dialog.showMessage(contents.head, "Registered " + fullnamefield.text + " to the list of customers.", "Customers registered")
        customersComboBox.peer.setModel(new ComboBox(GameStore.getCustomers().map(cust => cust.getId() + " | " + cust.getFullName())).peer.getModel)

        FileHandler.saveCustomer()
        replacePanel(new TextArea("dasdasdas"))
      } else Dialog.showMessage(contents.head, "Invalid credentials, please ensure all fields have values", "Invalid credentials")
    }
  }
  def registerItem = new BoxPanel(Orientation.Vertical) {
    val selitem = GameStore.getItemByID(itemsComboBox.selection.item.split('|').head.trim.toInt)

    val namfield = new TextField() {if (updateMode) text = selitem.getName()}
    val stockpricef = new TextField() {if (updateMode) text = selitem.getStockPrice().toString}
    val salepricef = new TextField() {if (updateMode) text = selitem.getSalePrice().toString}
    val stockfield = new TextField() {if (updateMode) text = selitem.getStockRemaining().toString}
    val typefield = new TextField() {if (updateMode) {
      text = selitem.getItemType()
      enabled = false
    }}

    val isgamecheck = new CheckBox() {if (updateMode) {
      selected = selitem match {
        case _ : Game => true
        case _ => false
      }
      enabled = false
    }}

    val yearfield = new TextField() {if (updateMode) {
      text = selitem match {
        case game : Game => game.getReleaseDate().getYear.toString
        case _ => ""
      }
      enabled = false
    }}
    val monthfield = new TextField() {if (updateMode) {
      text = selitem match {
        case game : Game => game.getReleaseDate().getMonthValue.toString
        case _ => ""
      }
      enabled = false
    }}
    val dayfield = new TextField() {if (updateMode) {
      text = selitem match {
        case game : Game => game.getReleaseDate().getDayOfMonth.toString
        case _ => ""
      }
      enabled = false
    }}
    val ratingfield = new TextField() {if (updateMode) {
      text = selitem match {
        case game : Game => game.getRating()
        case _ => ""
      }
      enabled = false
    }}

    contents += new GridPanel(8, 2) {
      contents += new Label("Item Name: ")
      contents += namfield
      contents += new Label("Stock Price: ")
      contents += stockpricef
      contents += new Label("Sale Price: ")
      contents += salepricef
      contents += new Label("Stock Quantity: ")
      contents += stockfield
      contents += new Label("Item Type: ")
      contents += typefield
      contents += new Label("Is Game: ")
      contents += isgamecheck
      contents += new Label("Release Date: ")
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += new Label("DD")
        contents += dayfield
        contents += new Label("MM")
        contents += monthfield
        contents += new Label("YYYY")
        contents += yearfield
      }
      contents += new Label("Game Rating: ")
      contents += ratingfield
    }

    val checkadditionals = isgamecheck.selected && (Try(dayfield.text.toInt).isSuccess && Try(monthfield.text.toInt).isSuccess && Try(yearfield.text.toInt).isSuccess && ratingfield.text != "") || !isgamecheck.selected
    if (updateMode) contents += Button("Update Item") {
      if (namfield.text != "" && salepricef.text != "" && stockpricef.text != "" && stockfield.text != "" && typefield.text != "" && checkadditionals) {
        selitem.setName(namfield.text)
        selitem.setStockPrice(stockpricef.text.toDouble)
        selitem.setSalePrice(salepricef.text.toDouble)
        selitem.setStockRemaining(stockfield.text.toInt)

        Dialog.showMessage(contents.head, "Updated item " + namfield.text + " details.", "Item updated")
        itemsComboBox.peer.setModel(new ComboBox(GameStore.getItems().map(item => item.getID() + " | " + item.getName())).peer.getModel)

        FileHandler.saveItems()
        replacePanel(new TextArea("dasdasdas"))
      }
    }
    else contents += Button("Add Item") {
      val localdatestr = yearfield.text+"-"+monthfield.text+"-"+dayfield.text
      if (namfield.text != "" && salepricef.text != "" && stockpricef.text != "" && stockfield.text != "" && typefield.text != "" && checkadditionals && Try(LocalDate.parse(localdatestr)).isSuccess) {
        if (isgamecheck.selected) {
          GameStore.registerItem(namfield.text, stockpricef.text.toDouble, salepricef.text.toDouble, stockfield.text.toInt, typefield.text, LocalDate.parse(localdatestr), ratingfield.text)
        } else {
          GameStore.registerItem(namfield.text, stockpricef.text.toDouble, salepricef.text.toDouble, stockfield.text.toInt, typefield.text)
        }
        Dialog.showMessage(contents.head, "Registered " + namfield.text + " to the list of items.", "Items registered")
        itemsComboBox.peer.setModel(new ComboBox(GameStore.getItems().map(item => item.getID() + " | " + item.getName())).peer.getModel)

        FileHandler.saveItems()
        replacePanel(new TextArea("dasdasdas"))
      } else Dialog.showMessage(contents.head, "Invalid credentials, please ensure all fields have values", "Invalid credentials")
    }
  }
  def dailyProfitPanel = new BoxPanel(Orientation.Vertical) {
    val yearfield = new TextField()
    val monthfield = new TextField()
    val dayfield = new TextField()

    contents += new GridPanel(1, 2) {
      contents += new Label("Date: ")
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += new Label("DD")
        contents += dayfield
        contents += new Label("MM")
        contents += monthfield
        contents += new Label("YYYY")
        contents += yearfield
      }
    }

    contents += Button("Get Profits") {
      val localdatestr = yearfield.text+"-"+monthfield.text+"-"+dayfield.text
      if (Try(LocalDate.parse(localdatestr)).isSuccess) {
        Dialog.showMessage(contents.head, "Daily profits for " + localdatestr + " : £" + GameStore.totalProfitForDay(localdatestr), "Daily Profits")
        replacePanel(new TextArea("dasdasdas"))
      }
    }
  }
  def dailyReceiptsPanel = new BoxPanel(Orientation.Vertical) {
    val yearfield = new TextField()
    val monthfield = new TextField()
    val dayfield = new TextField()

    contents += new GridPanel(1, 2) {
      contents += new Label("Date: ")
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += new Label("DD")
        contents += dayfield
        contents += new Label("MM")
        contents += monthfield
        contents += new Label("YYYY")
        contents += yearfield
      }
    }

    contents += Button("Daily Profits") {
      val localdatestr = yearfield.text+"-"+monthfield.text+"-"+dayfield.text
      if (Try(LocalDate.parse(localdatestr)).isSuccess) {
        new ReceiptsPanel(pack(), GameStore.dailyReceiptsByDate(localdatestr)).visible = true
      }
    }
  }

  def replacePanel(newComponent : Component): Unit = {
    currentPanel.contents -= curPanelContents
    curPanelContents = newComponent
    currentPanel.contents += curPanelContents
    currentPanel.revalidate()
    currentPanel.repaint()
  }

  contents = new BoxPanel(Orientation.Vertical) {
    contents += new Label(title)
    contents += new Separator()

    contents += new GridPanel(2, 3) {
        contents += new Label("Items")
        contents += new Label("Employees")
        contents += new Label("Customers")
        contents += itemsComboBox
        contents += employeesComboBox
        contents += customersComboBox
    }

    contents += new BoxPanel(Orientation.Horizontal) {
      contents += new BoxPanel(Orientation.Vertical) {
        contents += Button("Register Employee") {
          updateMode = false; replacePanel(registerEmployee)
        }
        contents += Button("Update Employee") {
          updateMode = true; replacePanel(registerEmployee)
        }
        contents += Button("Delete Employee") {
          val employid = employeesComboBox.selection.item.split('|').head.trim.toInt
          val selemploy = GameStore.getEmployeeByID(employid)
          val dodelete = Dialog.showConfirmation(contents.head, "Do you wish to delete "+selemploy.getFullName(), optionType=Dialog.Options.YesNo, title="Confirm deletion")
          if (dodelete == Dialog.Result.Yes) {
            GameStore.deleteEmployeeByID(employid)
            Dialog.showMessage(contents.head, "Deleted " + selemploy.getFullName() + " from the registered employees.", "Employee deleted")
            employeesComboBox.peer.setModel(new ComboBox(GameStore.getEmployees().map(emp => emp.getId() + " | " + emp.getFullName())).peer.getModel)

            FileHandler.saveEmployee()
            replacePanel(new TextArea("dasdasdas"))
          }
        }
        contents += Button("Register Customer") {
          updateMode = false; replacePanel(registerCustomer)
        }
        contents += Button("Update Customer") {
          updateMode = true; replacePanel(registerCustomer)
        }
        contents += Button("Delete Customer") {
          val customerid = customersComboBox.selection.item.split('|').head.trim.toInt
          val selcustomer = GameStore.getCustomerByID(customerid)
          val dodelete = Dialog.showConfirmation(contents.head, "Do you wish to delete "+selcustomer.getFullName(), optionType=Dialog.Options.YesNo, title="Confirm deletion")
          if (dodelete == Dialog.Result.Yes) {
            GameStore.deleteCustomerByID(customerid)
            Dialog.showMessage(contents.head, "Deleted " + selcustomer.getFullName() + " from the registered customers.", "Customer deleted")
            customersComboBox.peer.setModel(new ComboBox(GameStore.getCustomers().map(cust => cust.getId() + " | " + cust.getFullName())).peer.getModel)

            FileHandler.saveCustomer()
            replacePanel(new TextArea("dasdasdas"))
          }
        }
        contents += Button("Add Item") {
          updateMode = false; replacePanel(registerItem)
        }
        contents += Button("Update Item") {
          updateMode = true; replacePanel(registerItem)
        }
        contents += Button("Delete Item") {
          val iid = itemsComboBox.selection.item.split('|').head.trim.toInt
          val selitem = GameStore.getItemByID(iid)
          val dodelete = Dialog.showConfirmation(contents.head, "Do you wish to delete "+selitem.getName(), optionType=Dialog.Options.YesNo, title="Confirm deletion")
          if (dodelete == Dialog.Result.Yes) {
            GameStore.deleteItem(iid)
            Dialog.showMessage(contents.head, "Deleted " + selitem.getName() + " from the registered items.", "Item deleted")
            itemsComboBox.peer.setModel(new ComboBox(GameStore.getItems().map(item => item.getID() + " | " + item.getName())).peer.getModel)

            FileHandler.saveItems()
            replacePanel(new TextArea("dasdasdas"))
          }
        }
        contents += Button("Daily Profit") {
          replacePanel(dailyProfitPanel)
        }
        contents += Button("Daily Sales") {
          replacePanel(dailyReceiptsPanel)
        }
        contents += Button("Forecast Profit") {}

        // stack overflow snippet
        val maxWidth = contents map {
          (button: Component) => button.maximumSize
        } maxBy { _.width }

        contents foreach {
          (button: Component) => button.maximumSize = maxWidth
        }
      }
      contents += HStrut(150)

      contents += currentPanel
    }
  }
}
object ManagerGUIMain {
  def main(args: Array[String]) {
    val ui = new ManagerGUI
    ui.visible = true
  }
}