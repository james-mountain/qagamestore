import java.awt.Dimension

import scala.swing.{PasswordField, _}
import scala.swing.event._

class UI extends MainFrame {
  var user: Employee = null

  val emailField = new TextField {
    columns = 20
  }
  var passwordField = new PasswordField {
    columns = 20
  }

  var frame = new Frame() {
    title = "Login"
    preferredSize = new Dimension(350, 120)
    visible = true
    contents = login()
  }

  listenTo(emailField)
  listenTo(passwordField)

  def login(): Component = {
    //frame.title = "Login"
    val contents = new BoxPanel(Orientation.Vertical) {
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += new Label("Email Address: ")
        contents += emailField
      }

      contents += new BoxPanel(Orientation.Horizontal) {
        contents += new Label("Password:         ")
        contents += passwordField
      }
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += Swing.HGlue
        contents += Button("Login") {
          loginUser()
        }
        contents += Button("Close") {
          Close()
        }
      }
    } // end of main contents
    contents
  }

  def mainMenu(): Component = {
    frame.preferredSize = new Dimension(400, 200)
    frame.title = user.getFullName()
    val contents = new GridPanel(2, 2) {

      contents += new GridPanel(1, 2) {
        contents += new Button("Update Records")
        contents += new Button("Tills")
      }
      contents += new GridPanel(1, 3) {
        contents += Button("Logout") {
          logoutUser()
        }
        contents += Button("Close") {
          Close()
        }
      }
    }
    contents
  }

  def logoutUser() {
    user = null
    frame.title = "Login"
    frame.preferredSize = new Dimension(350, 120)
    frame.contents = login()
  }

  def loginUser() {
    val loginUser = GameStore.checkForUser(emailField.text, passwordField.password.mkString)
    passwordField.peer.setText("")
    emailField.text = ""
    if (loginUser != null) {
      user = loginUser
      if (user.getIsManager()) {
        frame.contents = mainMenu()
      }
    } else {
      println("Incorrect")
    }
  }

  def Close() {
    sys.exit(0)
  }
} // end of class

object GameStoreGui extends App {
  val ui = new UI
}
