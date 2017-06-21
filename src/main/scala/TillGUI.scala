import java.awt.Dimension

import scala.swing.event.{Key, KeyPressed}
import scala.swing.{BoxPanel, Button, ButtonGroup, ComboBox, Dialog, Label, MainFrame, Orientation, RadioButton, ScrollPane, Separator, Swing, Table, TextField}
import scala.util.Try

/**
  * Created by Administrator on 21/06/2017.
  */

class RegisterCustomerFromTill(tillGUI: TillGUI) extends MainFrame {
  val fullnamefield = new TextField() {
  }
  val emailfield = new TextField() {
  }

  preferredSize = new Dimension(300, 128)

  contents = new BoxPanel(Orientation.Vertical) {
    contents += new BoxPanel(Orientation.Horizontal) {
      contents += new Label("Full Name: ")
      contents += fullnamefield
    }

    contents += new BoxPanel(Orientation.Horizontal) {
      contents += new Label("Email: ")
      contents += emailfield
    }

    contents += Button("Register") {
      if (fullnamefield.text != "" && emailfield.text != "") {
        tillGUI.currentcustomer = Some(GameStore.registerCustomer(fullnamefield.text, emailfield.text))
        close()
      } else Dialog.showMessage(contents.head, "Invalid credentials, please ensure all fields have values", "Invalid credentials")
    }
  }
}

class TillGUI extends MainFrame {
  var loggedEmployee = new Employee(2, "Simon", "simon@hotmail.co.uk", true, "3434 House Street", "01234 562452", "password")
  var currentReceipt : Option[Receipt] = None
  var currentcustomer : Option[Customer] = None
  title = "Till Operations -------> Logged in as: " + loggedEmployee.getFullName()

  contents = new BoxPanel(Orientation.Vertical) {
    contents += new Label(title)
    contents += new Separator()

    val headers = Array("Name"," Quantity", "Price")
    val itemsComboBox = new ComboBox(GameStore.getItems().map(item => item.getID() + " | " + item.getName() + " £" + item.getSalePrice()))
    val emptyvals = Array.empty[Array[String]].map(_.toArray[Any])
    var receipttable = new Table(emptyvals, headers) {
      enabled = false
    }
    var scrollPane = new ScrollPane(receipttable)
    val totalField = new TextField() {
      columns = 10
      override def columns: Int = 10
      enabled = false
    }
    val totalPointsField = new TextField() {
      columns = 10
      override def columns: Int = 10
      enabled = false
    }
    val pointsToSpendField = new TextField() {
      columns = 10
      override def columns: Int = 10
      enabled = false
    }
    val quantityField = new TextField() {
      columns = 5
      override def columns: Int = 5
    }
    val customerEmailField = new TextField() {
      columns = 30
      override def columns: Int = 30
    }
    val radioButtons = List(new RadioButton("Card"), new RadioButton("Cash"))
    val buttongroup = new ButtonGroup(radioButtons: _*)

    val addItemButton = Button("Add Item") {
      if (Try(quantityField.text.toInt).isSuccess) {
        val itemid = itemsComboBox.selection.item.split('|').head.trim.toInt
        val item = GameStore.getItemByID(itemid)
        GameStore.addItemToReceipt(currentReceipt.get, item, quantityField.text.toInt)

        val model = receipttable.model
        // Get values out of the Table and put them into a vector list
        val receiptvectors = for (i <- 0 until model.getRowCount) yield (model.getValueAt(i, 0), model.getValueAt(i, 1), model.getValueAt(i, 2))

        // Add the existing vectors to the newly added item together in an array (pretty messy code)
        val receiptarrays = receiptvectors.map(e => Array(e._1, e._2, e._3)).toArray :+ Array(item.getName(), quantityField.text.toInt, item.getSalePrice() * quantityField.text.toInt)
        val receipts = receiptarrays.map(_.toArray[Any])

        receipttable = new Table(receipts, headers)
        scrollPane.viewportView = receipttable

        totalField.text = currentReceipt.get.getTotal.toString
      }
    }
    val checkoutButton = Button("Checkout") {
      buttongroup.selected match {
        case Some(button) => currentReceipt.get.setPaymentType(buttongroup.selected.get.text.toLowerCase)
        case _ => {}
      }

      if (GameStore.closeReceipt(currentReceipt.get, currentcustomer)) {
        receipttable = new Table(emptyvals, headers)
        scrollPane.viewportView = receipttable
        totalField.text = ""
        totalPointsField.text = ""
        addItemButton.enabled = false
        enabled = false

        Dialog.showMessage(contents.head, "Transaction complete and saved", "Transaction Complete")
      }
    }
    addItemButton.enabled = false
    checkoutButton.enabled = false

    contents += new BoxPanel(Orientation.Horizontal) {
      contents += Button("Start Sale") {
        currentReceipt = Some(GameStore.createNewReceipt())
        addItemButton.enabled = true
        checkoutButton.enabled = true
      }

      contents += itemsComboBox
      contents += new Label("Quantity: ")
      contents += quantityField
      contents += addItemButton
    }
    contents += new Separator()

    contents += scrollPane
    contents += new Separator()

    contents += new BoxPanel(Orientation.Horizontal) {
      contents += new Label("Customer Email: ")
      contents += customerEmailField

      contents += new BoxPanel(Orientation.Horizontal) {
        contents ++= radioButtons
      }

      contents += new Label("Total Price: ")
      contents += totalField
      contents += checkoutButton
    }
    contents += new Separator()

    contents += new BoxPanel(Orientation.Horizontal) {
      contents += new Label("Total Points: ")
      contents += totalPointsField
      contents += new Label("Points to Spend: ")
      contents += pointsToSpendField

      contents += Button("Apply Discount") {
        GameStore.applyDiscount(currentReceipt.get, currentcustomer.get, pointsToSpendField.text.toInt)
        totalPointsField.text = currentcustomer.get.getMembershipPoints().toString;
        pointsToSpendField.text = ""
        totalField.text = currentReceipt.get.getTotal().toString
      }
    }

    listenTo(customerEmailField.keys)

    reactions += {
      case KeyPressed(_, Key.Enter, _, _) => GameStore.getCustomerByEmail(customerEmailField.text) match {
        case Some(customer) => totalPointsField.text = customer.getMembershipPoints().toString; pointsToSpendField.enabled = true; currentcustomer = Some(customer)
        case _ => {
          val doregister = Dialog.showConfirmation(contents.head, "No customer was found, do you want to register them?", optionType=Dialog.Options.YesNo, title="No customer found")
          if (doregister == Dialog.Result.Yes) new RegisterCustomerFromTill(pack()).visible = true
        }
      }
    }

    for (e <- contents)
      e.xLayoutAlignment = 0.0
    border = Swing.EmptyBorder(10, 10, 10, 10)
  }
}

object TillGUIMain {
  def main(args: Array[String]) {
    val ui = new TillGUI
    ui.visible = true
  }
}