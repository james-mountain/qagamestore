import java.awt.Dimension

import scala.collection.mutable.ListBuffer
import scala.swing.event.{Key, KeyPressed}
import scala.swing.{BoxPanel, Button, ButtonGroup, ComboBox, Component, Dialog, Frame, Label, MainFrame, Orientation, RadioButton, ScrollPane, Separator, Swing, Table, TextField}
import scala.util.Try

/**
  * Created by Administrator on 21/06/2017.
  */

class RegisterCustomerFromTill(tillGUI: TillGUI) extends MainFrame {
  val fullNameField = new TextField() {
  }
  val emailField = new TextField() {
  }

  preferredSize = new Dimension(300, 128)

  contents = new BoxPanel(Orientation.Vertical) {
    contents += new BoxPanel(Orientation.Horizontal) {
      contents += new Label("Full Name: ")
      contents += fullNameField
    }

    contents += new BoxPanel(Orientation.Horizontal) {
      contents += new Label("Email: ")
      contents += emailField
    }

    contents += Button("Register") {
      if (fullNameField.text != "" && emailField.text != "") {
        tillGUI.currentCustomer = Some(GameStore.registerCustomer(fullNameField.text, emailField.text))
        close()
      } else Dialog.showMessage(contents.head, "Invalid credentials, please ensure all fields have values", "Invalid credentials")
    }
  }
}

class TillGUI(user: Employee) extends MainFrame {
  var loggedEmployee: Employee = user
  var currentReceipt: Option[Receipt] = None
  var currentCustomer: Option[Customer] = None
  var preOrderList: ListBuffer[Int] = new ListBuffer
  var backButton: Button = if (loggedEmployee.getIsManager()) {
    Button("Back") {
      ScreenManager.menu(loggedEmployee)
    }
  } else {
    Button("Logout") {
      ScreenManager.login()
    }
  }

  var frame = new Frame() {
    title = "Till Operations -------> Logged in as: " + loggedEmployee.getFullName()
    visible = true
    contents = till()
    centerOnScreen()
    resizable = false
  }

  def till(): Component = {

    var contents = new BoxPanel(Orientation.Vertical) {
      contents += new Label(title)
      contents += new Separator()


      val headers = Array("Name", " Quantity", "Price")
      val itemsComboBox = new ComboBox(GameStore.getItems().map(item => item.getID() + " | " + item.getName() + " £" + item.getSalePrice()))
      val emptyValues: Array[Array[Any]] = Array.empty[Array[String]].map(_.toArray[Any])
      var receiptTable: Table = new Table(emptyValues, headers) {
        enabled = false
      }
      var scrollPane = new ScrollPane(receiptTable)
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
      val buttonGroup = new ButtonGroup(radioButtons: _*)

      val addItemButton: Button = Button("Add Item") {
        if (Try(quantityField.text.toInt).isSuccess && quantityField.text.toInt > 0) {
          val itemId = itemsComboBox.selection.item.split('|').head.trim.toInt
          val item = GameStore.getItemByID(itemId)

          //check for game pre-order
          item match {
            case game: Game => if (!game.getReleased()) {
              preOrderList += itemId
              println("pre ordered")
            }
            case _ => //do nothing
          }

          GameStore.addItemToReceipt(currentReceipt.get, item, quantityField.text.toInt) match {
            case true => {
              val model = receiptTable.model
              // Get values out of the Table and put them into a vector list
              val receiptVectors = for (i <- 0 until model.getRowCount) yield (model.getValueAt(i, 0), model.getValueAt(i, 1), model.getValueAt(i, 2))

              // Add the existing vectors to the newly added item together in an array (pretty messy code)
              val receiptArrays = receiptVectors.map(e => Array(e._1, e._2, e._3)).toArray :+ Array(item.getName(), quantityField.text.toInt, item.getSalePrice() * quantityField.text.toInt)
              val receipts = receiptArrays.map(_.toArray[Any])

              receiptTable = new Table(receipts, headers)
              scrollPane.viewportView = receiptTable

              totalField.text = currentReceipt.get.getTotal.toString
            }
            case false => Dialog.showMessage(contents.head, "Not enough stock for the desired quantity.", "Couldn't add item to receipt.")
          }
        }
      }
      val checkoutButton = Button("Checkout") {
        buttonGroup.selected match {
          case Some(button) => currentReceipt.get.setPaymentType(buttonGroup.selected.get.text.toLowerCase)
          case _ => {}
        }
        if (preOrderList.size == 0 || currentCustomer != None) {
          if (GameStore.closeReceipt(currentReceipt.get, currentCustomer)) {
            receiptTable = new Table(emptyValues, headers)
            scrollPane.viewportView = receiptTable
            totalField.text = ""
            totalPointsField.text = ""
            addItemButton.enabled = false
            enabled = false

            if (preOrderList.size != 0) {
              for (i <- 0 until preOrderList.size) {
                currentCustomer.get.addPreOrder(preOrderList(i))
              }
              preOrderList.remove(0, preOrderList.size - 1)
            }
            //remove the customer from the table
            currentCustomer = None
            customerEmailField.text = ""
            Dialog.showMessage(contents.head, "Transaction complete and saved", "Transaction Complete")
          }
        } else {
          Dialog.showMessage(contents.head, "Transaction incomplete. pre-orders require customer account", "Transaction Incomplete")
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
          if (Try(pointsToSpendField.text.toInt).isSuccess && pointsToSpendField.text.toInt > 0) {
            GameStore.applyDiscount(currentReceipt.get, currentCustomer.get, pointsToSpendField.text.toInt)
            totalPointsField.text = currentCustomer.get.getMembershipPoints().toString
            pointsToSpendField.text = ""
            totalField.text = currentReceipt.get.getTotal().toString
          }
        }

        contents += new BoxPanel(Orientation.Vertical) {
          contents += backButton
        }
      }

      listenTo(customerEmailField.keys)

      reactions += {
        case KeyPressed(_, Key.Enter, _, _) => GameStore.getCustomerByEmail(customerEmailField.text) match {
          case Some(customer) => totalPointsField.text = customer.getMembershipPoints().toString; pointsToSpendField.enabled = true; currentCustomer = Some(customer)
          case _ => {
            val doRegister = Dialog.showConfirmation(contents.head, "No customer was found, do you want to register them?", optionType = Dialog.Options.YesNo, title = "No customer found")
            if (doRegister == Dialog.Result.Yes) new RegisterCustomerFromTill(pack()).visible = true
          }
        }
      }

      for (e <- contents)
        e.xLayoutAlignment = 0.0
      border = Swing.EmptyBorder(10, 10, 10, 10)
    }
    contents
  }

  def delete(): Unit = {
    frame.dispose()
  }
}
