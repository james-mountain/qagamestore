import java.awt.Dimension
import java.awt.color

import scala.collection.mutable.ListBuffer
import scala.swing.{PasswordField, _}
import scala.swing.event._
import scala.swing.event.{Key, KeyPressed}
import scala.swing.{BoxPanel, Button, ButtonGroup, ComboBox, Dialog, Label, MainFrame, Orientation, RadioButton, ScrollPane, Separator, Swing, Table, TextField}
import scala.util.Try

class LoginUI extends MainFrame {
  var user: Employee = null
  val emailField = new TextField {
    columns = 20
  }
  val passwordField = new PasswordField {
    columns = 20
  }
  val message = new Label("")
  var closeButton = new Button("Close")
  var frame = new Frame() {
    title = "Login"
    preferredSize = new Dimension(500, 200)
    visible = true
    contents = login()
    resizable = false
    centerOnScreen()
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
        ScreenManager.menu(user)
      } else {
        message.text = ""
        ScreenManager.till(user)
      }
    } else {
      println("Incorrect")
      message.text = "Incorrect email and password, try again"
    }
  }

  def delete(): Unit = {
    frame.dispose()
  }

  def Close() {
    sys.exit(0)
  }
} // end of class

