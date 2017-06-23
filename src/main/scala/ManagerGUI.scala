import java.awt.Dimension
import java.time.LocalDate

import scala.collection.mutable.ListBuffer
import scala.swing.ListView.Renderer
import scala.swing.{BoxPanel, Button, CheckBox, ComboBox, Component, Dialog, FlowPanel, Frame, GridPanel, Label, ListView, MainFrame, Orientation, PasswordField, ScrollPane, Separator, TextArea, TextField}
import scala.swing.Swing.HStrut
import scala.util.Try

/**
  * Created by Administrator on 22/06/2017.
  */

class ReceiptsPanel(managerGUI: ManagerGUI, receipts : ListBuffer[Receipt]) extends MainFrame {
  preferredSize = new Dimension(700, 350)
  centerOnScreen()
  contents = new BoxPanel(Orientation.Vertical) {
    contents += new FlowPanel(new ScrollPane(new ListView(receipts) {
      renderer = Renderer(_.toString())
    }))
    contents += Button("Close") {close()}
  }
}

class ManagerGUI(user: Employee) extends MainFrame {

  var loggedEmployee = user
  val placeholder = "Select an option from the left menu."
  var itemsComboBox = new ComboBox(GameStore.getItems().map(item => item.getID() + " | " + item.getName()))
  var employeesComboBox = new ComboBox(GameStore.getEmployees().map(emp => emp.getId() + " | " + emp.getFullName()))
  var customersComboBox = new ComboBox(GameStore.getCustomers().map(cust => cust.getId() + " | " + cust.getFullName()))

  var updateMode = false

  var curPanelContents : Component = new TextArea(placeholder)
  var currentPanel = new BoxPanel(Orientation.Vertical) {
    contents += curPanelContents
  }

  var backButton: Button = Button("Back") {
    ScreenManager.menu(loggedEmployee)
  }

  var frame = new Frame(){
    title = "Manager Operations -------> Logged in as: " + loggedEmployee.getFullName()
    visible = true
    contents = manager()
    centerOnScreen()
  }

  def registerEmployee = new BoxPanel(Orientation.Vertical) {
    val selemploy = employeesComboBox.selection.item.split('|').headOption match {
      case Some(pemp) => Some(GameStore.getEmployeeByID(pemp.trim.toInt))
      case _ => None
    }

    val fullnamefield = new TextField() {if (updateMode) selemploy match {
      case Some(emp) => text = emp.getFullName()
      case _ => {}
    }}
    val emailfield = new TextField() {if (updateMode) selemploy match {
      case Some(emp) => text = emp.getEmail()
      case _ => {}
    }}
    val ismanagercheck = new CheckBox() {if (updateMode) selemploy match {
      case Some(emp) => selected = emp.getIsManager()
      case _ => {}
    }}
    val addressfield = new TextField() {if (updateMode) selemploy match {
      case Some(emp) => text = emp.getAddress()
      case _ => {}
    }}
    val telfield = new TextField() {if (updateMode) selemploy match {
      case Some(emp) => text = emp.getTel()
      case _ => {}
    }}
    val passfield = new PasswordField(if (updateMode) selemploy match {
      case Some(emp) => emp.getPass()
      case _ => ""
    } else "")

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
        selemploy match {
          case Some(emp) => {
            emp.setFullName(fullnamefield.text)
            emp.setEmail(emailfield.text)
            emp.setIsManager(ismanagercheck.selected)
            emp.setAddress(addressfield.text)
            emp.setTel(telfield.text)
            emp.setPass(passfield.password.mkString)
          }
          case _ => {}
        }
        Dialog.showMessage(contents.head, "Updated " + fullnamefield.text + "'s details.", "Employee updated")
        employeesComboBox.peer.setModel(new ComboBox(GameStore.getEmployees().map(emp => emp.getId() + " | " + emp.getFullName())).peer.getModel)

        FileHandler.saveEmployee()
        replacePanel(new TextArea(placeholder))
      }
    }
    else contents += Button("Register") {
      if (fullnamefield.text != "" && emailfield.text != "" && addressfield.text != "" && telfield.text != "" && passfield.password.mkString != "") {
        GameStore.registerEmployee(fullnamefield.text, emailfield.text, ismanagercheck.selected, addressfield.text, telfield.text, passfield.password.mkString)
        Dialog.showMessage(contents.head, "Registered " + fullnamefield.text + " to the list of employees.", "Employee registered")
        employeesComboBox.peer.setModel(new ComboBox(GameStore.getEmployees().map(emp => emp.getId() + " | " + emp.getFullName())).peer.getModel)

        FileHandler.saveEmployee()
        replacePanel(new TextArea(placeholder))
      } else Dialog.showMessage(contents.head, "Invalid credentials, please ensure all fields have values", "Invalid credentials")
    }
  }
  def registerCustomer = new BoxPanel(Orientation.Vertical) {
    val selcustomer = customersComboBox.selection.item.split('|').headOption match {
      case Some(pcust) => Some(GameStore.getCustomerByID(pcust.trim.toInt))
      case _ => None
    }

    val fullnamefield = new TextField() {if (updateMode) selcustomer match {
      case Some(cust) => text = cust.getFullName()
      case _ => {}
    }}
    val emailfield = new TextField() {if (updateMode) selcustomer match {
      case Some(cust) => text = cust.getEmail()
      case _ => {}
    }}

    contents += new GridPanel(6, 2) {
      contents += new Label("Full Name: ")
      contents += fullnamefield
      contents += new Label("Email: ")
      contents += emailfield
    }

    if (updateMode) contents += Button("Update Customer") {
      if (fullnamefield.text != "" && emailfield.text != "") {
        selcustomer match {
          case Some(cust) => {
            cust.setFullName(fullnamefield.text)
            cust.setEmail(emailfield.text)
          }
          case _ => {}
        }
        Dialog.showMessage(contents.head, "Updated " + fullnamefield.text + "'s details.", "Customer updated")
        customersComboBox.peer.setModel(new ComboBox(GameStore.getCustomers().map(cust => cust.getId() + " | " + cust.getFullName())).peer.getModel)

        FileHandler.saveCustomer()
        replacePanel(new TextArea(placeholder))
      }
    }
    else contents += Button("Register") {
      if (fullnamefield.text != "" && emailfield.text != "") {
        GameStore.registerCustomer(fullnamefield.text, emailfield.text)
        Dialog.showMessage(contents.head, "Registered " + fullnamefield.text + " to the list of customers.", "Customers registered")
        customersComboBox.peer.setModel(new ComboBox(GameStore.getCustomers().map(cust => cust.getId() + " | " + cust.getFullName())).peer.getModel)

        FileHandler.saveCustomer()
        replacePanel(new TextArea(placeholder))
      } else Dialog.showMessage(contents.head, "Invalid credentials, please ensure all fields have values", "Invalid credentials")
    }
  }
  def registerItem = new BoxPanel(Orientation.Vertical) {
    val selitem = itemsComboBox.selection.item.split('|').headOption match {
      case Some(pitem) => Some(GameStore.getItemByID(pitem.trim.toInt))
      case _ => None
    }

    val namfield = new TextField() {if (updateMode) selitem match {
      case Some(item) => text = item.getName()
      case _ => {}
    }}
    val stockpricef = new TextField() {if (updateMode) selitem match {
      case Some(item) => text = item.getStockPrice().toString
      case _ => {}
    }}
    val salepricef = new TextField() {if (updateMode) selitem match {
      case Some(item) => text = item.getSalePrice().toString
      case _ => {}
    }}
    val stockfield = new TextField() {if (updateMode) selitem match {
      case Some(item) => text = item.getStockRemaining().toString
      case _ => {}
    }}
    val typefield = new TextField() {if (updateMode) {
      selitem match {
        case Some(item) => text = item.getItemType()
        case _ => {}
      }
      enabled = false
    }}

    val isgamecheck = new CheckBox() {if (updateMode) {
      selected = selitem match {
        case Some(_ : Game) => true
        case _ => false
      }
      enabled = false
    }}

    val yearfield = new TextField() {if (updateMode) {
      text = selitem match {
        case Some(game : Game) => game.getReleaseDate().getYear.toString
        case _ => ""
      }
      enabled = false
    }}
    val monthfield = new TextField() {if (updateMode) {
      text = selitem match {
        case Some(game : Game) => game.getReleaseDate().getMonthValue.toString
        case _ => ""
      }
      enabled = false
    }}
    val dayfield = new TextField() {if (updateMode) {
      text = selitem match {
        case Some(game : Game) => game.getReleaseDate().getDayOfMonth.toString
        case _ => ""
      }
      enabled = false
    }}
    val ratingfield = new TextField() {if (updateMode) {
      text = selitem match {
        case Some(game : Game) => game.getRating()
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

    def checkadditionals = isgamecheck.selected && (Try(dayfield.text.toInt).isSuccess && Try(monthfield.text.toInt).isSuccess && Try(yearfield.text.toInt).isSuccess && ratingfield.text != "") || !isgamecheck.selected
    def checknumbervalid = Try(stockpricef.text.toDouble).isSuccess && stockpricef.text.toDouble > 0.00 &&
      Try(salepricef.text.toDouble).isSuccess && salepricef.text.toDouble > 0.00 &&
      Try(stockfield.text.toInt).isSuccess && stockfield.text.toInt > 0

    println(checkadditionals, checknumbervalid)
    if (updateMode) contents += Button("Update Item") {
      if (namfield.text != "" && salepricef.text != "" && stockpricef.text != "" && stockfield.text != "" && typefield.text != "" && checkadditionals && checknumbervalid) {
        selitem match {
          case Some(item) => {
            item.setName(namfield.text)
            item.setStockPrice(stockpricef.text.toDouble)
            item.setSalePrice(salepricef.text.toDouble)
            item.setStockRemaining(stockfield.text.toInt)
          }
          case _ => {}
        }

        Dialog.showMessage(contents.head, "Updated item " + namfield.text + " details.", "Item updated")
        itemsComboBox.peer.setModel(new ComboBox(GameStore.getItems().map(item => item.getID() + " | " + item.getName())).peer.getModel)

        FileHandler.saveItems()
        replacePanel(new TextArea(placeholder))
      }
    }
    else contents += Button("Add Item") {
      val localdatestr = yearfield.text+"-"+monthfield.text+"-"+dayfield.text
      if (namfield.text != "" && salepricef.text != "" && stockpricef.text != "" && stockfield.text != "" && typefield.text != "" && checkadditionals && Try(LocalDate.parse(localdatestr)).isSuccess && checknumbervalid) {
        if (isgamecheck.selected) {
          GameStore.registerItem(namfield.text, stockpricef.text.toDouble, salepricef.text.toDouble, stockfield.text.toInt, typefield.text, LocalDate.parse(localdatestr), ratingfield.text)
        } else {
          GameStore.registerItem(namfield.text, stockpricef.text.toDouble, salepricef.text.toDouble, stockfield.text.toInt, typefield.text)
        }
        Dialog.showMessage(contents.head, "Registered " + namfield.text + " to the list of items.", "Items registered")
        itemsComboBox.peer.setModel(new ComboBox(GameStore.getItems().map(item => item.getID() + " | " + item.getName())).peer.getModel)

        FileHandler.saveItems()
        replacePanel(new TextArea(placeholder))
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
        replacePanel(new TextArea(placeholder))
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

    contents += Button("Daily Receipts") {
      val localdatestr = yearfield.text+"-"+monthfield.text+"-"+dayfield.text
      if (Try(LocalDate.parse(localdatestr)).isSuccess) {
        new ReceiptsPanel(pack(), GameStore.dailyReceiptsByDate(localdatestr)).visible = true
      }
    }
  }

  def forecastProfitsPanel = new BoxPanel(Orientation.Vertical) {
    val yearfield = new TextField()
    val monthfield = new TextField()
    val dayfield = new TextField()
    val yearfield2 = new TextField()
    val monthfield2 = new TextField()
    val dayfield2 = new TextField()

    contents += new GridPanel(2, 2) {
      contents += new Label("Start Date: ")
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += new Label("DD")
        contents += dayfield
        contents += new Label("MM")
        contents += monthfield
        contents += new Label("YYYY")
        contents += yearfield
      }
      contents += new Label("End Date: ")
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += new Label("DD")
        contents += dayfield2
        contents += new Label("MM")
        contents += monthfield2
        contents += new Label("YYYY")
        contents += yearfield2
      }
    }

    contents += Button("Forecasted Profits") {
      val localdatestr = yearfield.text+"-"+monthfield.text+"-"+dayfield.text
      val localdate2str = yearfield2.text+"-"+monthfield2.text+"-"+dayfield2.text
      if (Try(LocalDate.parse(localdatestr)).isSuccess && Try(LocalDate.parse(localdate2str)).isSuccess) {
        Dialog.showMessage(contents.head, "Forecasted profits for " + localdatestr + " to " + localdate2str + ": £" + GameStore.forecastProfits(LocalDate.parse(localdatestr), LocalDate.parse(localdate2str)), "Forecasted Profits")
        replacePanel(new TextArea(placeholder))
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

  def manager():Component= {
    var contents = new BoxPanel(Orientation.Vertical) {
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
            updateMode = false;
            replacePanel(registerEmployee)
          }
          contents += Button("Update Employee") {
            employeesComboBox.selection.item.split('|').headOption match {
              case Some(empid) => {
                updateMode = true;
                replacePanel(registerEmployee)
              }
              case _ =>
            }
          }
          contents += Button("Delete Employee") {
            employeesComboBox.selection.item.split('|').headOption match {
              case Some(empid) => {
                val selemploy = GameStore.getEmployeeByID(empid.trim.toInt)
                val dodelete = Dialog.showConfirmation(contents.head, "Do you wish to delete " + selemploy.getFullName(), optionType = Dialog.Options.YesNo, title = "Confirm deletion")
                if (dodelete == Dialog.Result.Yes) {
                  GameStore.deleteEmployeeByID(empid.trim.toInt)
                  Dialog.showMessage(contents.head, "Deleted " + selemploy.getFullName() + " from the registered employees.", "Employee deleted")
                  employeesComboBox.peer.setModel(new ComboBox(GameStore.getEmployees().map(emp => emp.getId() + " | " + emp.getFullName())).peer.getModel)

                  FileHandler.saveEmployee()
                  replacePanel(new TextArea(placeholder))
                }
              }
              case _ =>
            }
          }
          contents += Button("Register Customer") {
            updateMode = false;
            replacePanel(registerCustomer)
          }
          contents += Button("Update Customer") {
            customersComboBox.selection.item.split('|').headOption match {
              case Some(cid) => {
                updateMode = true;
                replacePanel(registerCustomer)
              }
              case _ =>
            }
          }
          contents += Button("Delete Customer") {
            customersComboBox.selection.item.split('|').headOption match {
              case Some(cid) => {
                val selcustomer = GameStore.getCustomerByID(cid.trim.toInt)
                val dodelete = Dialog.showConfirmation(contents.head, "Do you wish to delete " + selcustomer.getFullName(), optionType = Dialog.Options.YesNo, title = "Confirm deletion")
                if (dodelete == Dialog.Result.Yes) {
                  GameStore.deleteCustomerByID(cid.trim.toInt)
                  Dialog.showMessage(contents.head, "Deleted " + selcustomer.getFullName() + " from the registered customers.", "Customer deleted")
                  customersComboBox.peer.setModel(new ComboBox(GameStore.getCustomers().map(cust => cust.getId() + " | " + cust.getFullName())).peer.getModel)

                  FileHandler.saveCustomer()
                  replacePanel(new TextArea(placeholder))
                }
              }
              case _ =>
            }
          }
          contents += Button("Add Item") {
            updateMode = false;
            replacePanel(registerItem)
          }
          contents += Button("Update Item") {
            itemsComboBox.selection.item.split('|').headOption match {
              case Some(iid) => {
                updateMode = true;
                replacePanel(registerItem)
              }
              case _ =>
            }
          }
          contents += Button("Delete Item") {
            itemsComboBox.selection.item.split('|').headOption match {
              case Some(iid) => {
                val selitem = GameStore.getItemByID(iid.trim.toInt)
                val dodelete = Dialog.showConfirmation(contents.head, "Do you wish to delete " + selitem.getName(), optionType = Dialog.Options.YesNo, title = "Confirm deletion")
                if (dodelete == Dialog.Result.Yes) {
                  GameStore.deleteItem(iid.trim.toInt)
                  Dialog.showMessage(contents.head, "Deleted " + selitem.getName() + " from the registered items.", "Item deleted")
                  itemsComboBox.peer.setModel(new ComboBox(GameStore.getItems().map(item => item.getID() + " | " + item.getName())).peer.getModel)

                  FileHandler.saveItems()
                  replacePanel(new TextArea(placeholder))
                }
              }
              case _ =>
            }
          }
          contents += Button("Daily Profit") {
            replacePanel(dailyProfitPanel)
          }
          contents += Button("Daily Sales") {
            replacePanel(dailyReceiptsPanel)
          }
          contents += Button("Forecast Profit") {
            replacePanel(forecastProfitsPanel)
          }

          // stack overflow snippet
          val maxWidth = contents map {
            (button: Component) => button.maximumSize
          } maxBy {
            _.width
          }

          contents foreach {
            (button: Component) => button.maximumSize = maxWidth
          }
        }
        contents += HStrut(150)
        contents += currentPanel
      }

      contents += new BoxPanel(Orientation.Horizontal) {
        contents += HStrut(600)
        contents += backButton
      }
    }
    contents
  }

  def delete(): Unit = {
    frame.dispose()
  }
}