import java.awt.Dimension

import scala.swing._
import scala.swing.event._

class UI extends MainFrame {
  var user: Employee = null

  val emailField = new TextField {
    columns = 20
  }
  val passwordField = new PasswordField {
    columns = 20
  }

  var frame = new Frame() {
    title = "Game store"
    preferredSize = new Dimension(800, 800)
    visible = true
    contents = login()
  }

  def login(): Component = {
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
          //login functionality
          loginUser()

        }
        contents += Button("Close") {
          Close()
        }
      }
    } // end of main contents
    contents
  }

  listenTo(emailField)
  listenTo(passwordField)

  reactions += {
    case ButtonClicked(s) =>
      println("Button click on button: '" + s.text + "'")
  }

  def mainMenu(): Component = {
    val contents = new GridPanel(2, 2) {

      contents += new GridPanel(1, 2) {
        contents += new Button("Admin")
        contents += new Button("Staff")
      }
      contents += new GridPanel(1, 3) {
        contents += new Button("Logout") {

        }
        contents += Button("Close") {
          Close()
        }
      }
    }
    contents
  }


  def loginUser() {
    val loginUser = GameStore.checkForUser(emailField.text, passwordField.password.mkString)
    if (loginUser != null) {
      user = loginUser
      frame.contents = mainMenu()
    } else {
      println("Incorrect")
    }
  }

  def Close() {
    println("Your email: " + emailField.text)
    println("Your password: " + passwordField.password.mkString)
    sys.exit(0)
  }
} // end of class

object GameStoreGui {
  def main(args: Array[String]) {
    val ui = new UI
  }
}
