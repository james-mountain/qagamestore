import scala.swing.{BoxPanel, Button, ButtonGroup, ComboBox, Dialog, Label, MainFrame, Orientation, RadioButton, ScrollPane, Separator, Swing, Table, TextField}
import scala.util.Try

/**
  * Created by Administrator on 21/06/2017.
  */
class TillGUI extends MainFrame {
  var loggedEmployee = new Employee(2, "Simon", "simon@hotmail.co.uk", true, "3434 House Street", "01234 562452", "password")
  var currentReceipt : Option[Receipt] = None
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
    val quantityField = new TextField() {
      columns = 5
      override def columns: Int = 5
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

      if (GameStore.closeReceipt(currentReceipt.get)) {
        receipttable = new Table(emptyvals, headers)
        scrollPane.viewportView = receipttable
        totalField.text = ""
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
      contents += new TextField() {
        columns = 30
        override def columns: Int = 30
      }

      contents += new BoxPanel(Orientation.Horizontal) {
        contents ++= radioButtons
      }

      contents += new Label("Total Price: ")
      contents += totalField
      contents += checkoutButton
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