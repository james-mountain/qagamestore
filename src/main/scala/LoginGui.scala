import java.awt.Dimension
import java.awt.color
import scala.swing.{PasswordField, _}
import scala.swing.event._
import scala.swing.event.{Key, KeyPressed}
import scala.swing.{BoxPanel, Button, ButtonGroup, ComboBox, Dialog, Label, MainFrame, Orientation, RadioButton, ScrollPane, Separator, Swing, Table, TextField}
import scala.util.Try

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

class UI extends MainFrame {
  var user: Employee = null

  val emailField = new TextField {
    columns = 20
  }
  val passwordField = new PasswordField {
    columns = 20
  }

  val message = new Label("")

  var adminButton = new Button("Update Records"){
    maximumSize = new Dimension(200, 175)
  }
  var staffButton = new Button("Tills"){
    maximumSize = new Dimension(200, 175)
  }
  var logoutButton = new Button("Logout")
  var closeButton = new Button("Close")

  var frame = new Frame() {
    title = "Login"
    preferredSize = new Dimension(500, 200)
    visible = true
    contents = login()
    resizable = false
  }

  listenTo(emailField)
  listenTo(passwordField)

  ///////////////////////////////////////////////// Windows / GUIs ///////////////////////////////////////////////

  def login(): Component = {
    val contents = new BoxPanel(Orientation.Vertical) {
      contents += Swing.VStrut(20)
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += Swing.HStrut(30)
        contents += new Label("Email Address: ")
        contents += emailField
        contents += Swing.HStrut(30)
      }

      contents += Swing.VStrut(20)
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += Swing.HStrut(30)
        contents += new Label("Password:         ")
        contents += passwordField
        contents += Swing.HStrut(30)
      }

      contents += Swing.VStrut(20)
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += Swing.HStrut(30)
        message.peer.setForeground(new Color(255, 0, 0))
        contents += message
        contents += Swing.HGlue
        contents += Button("Login") {
          loginUser()
        }
        contents += Swing.HStrut(20)
        contents += Button("Close") {
          Close()
        }
        contents += Swing.HStrut(30)
      }
      contents += Swing.VStrut(20)

    } // end of main contents
    contents
  }

  def mainMenu(): Component = {
    frame.preferredSize = new Dimension(400, 200)
    frame.title = user.getFullName()
    val contents = new BoxPanel(Orientation.Vertical) {
      contents += Swing.VStrut(20)
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += Swing.HStrut(30)
        contents += adminButton
        contents += Swing.HStrut(30)
        contents += staffButton
        contents += Swing.HStrut(30)
      }

      contents += Swing.VStrut(20)
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += Swing.HStrut(30)
        contents += Button("Logout") {
          logoutUser()
        }
        contents += Swing.HStrut(30)
        contents += Button("Close") {
          Close()
        }
        contents += Swing.HStrut(30)
      }
      contents += Swing.VStrut(20)
    }
    contents
  }

//  def till(): Component = {
//
//  }
///////////////////////////////////////////////// Functionality ///////////////////////////////////////////////
  def logoutUser() {
    user = null
    frame.title = "Login"
    frame.preferredSize = new Dimension(500, 200)
    frame.contents = login()
  }

  def loginUser() {
    val loginUser = GameStore.checkForUser(emailField.text, passwordField.password.mkString)
    passwordField.peer.setText("")
    emailField.text = ""
    if (loginUser != null) {
      user = loginUser
      if (user.getIsManager()) {
        message.text = ""
        frame.contents = mainMenu()
      }
    } else {
      println("Incorrect")
      message.text = "Incorrect email and password, try again"
    }
  }

  def Close() {
    sys.exit(0)
  }
} // end of class

object GameStoreGui extends App {
  val ui = new UI
}
