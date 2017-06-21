import scala.swing._
import scala.swing.event._

class UI extends MainFrame {

  title = "Game Store"

  val emailField = new TextField {
    columns = 20
  }
  val passwordField = new TextField {
    columns = 20
  }
  val gamestore = new GameStore

  contents = new BoxPanel(Orientation.Vertical) {
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
        val loginUser = gamestore.checkForUser(emailField.text, passwordField.text)
        if (loginUser != null) {
          println("Successful Login")
        } else {
          println("Incorrect")
        }
      }

      contents += Button("Close") {
        reportAndClose()
      }
    }

    for (e <- contents)
      e.xLayoutAlignment = 0.0
    border = Swing.EmptyBorder(10, 10, 10, 10)
  } // end of main contents

  listenTo(emailField)
  listenTo(passwordField)


  reactions += {
    case ButtonClicked(s) =>
      println("Button click on button: '" + s.text + "'")
  }

  // close button functionality
  def reportAndClose() {
    println("Your email: " + emailField.text)
    println("Your password: " + passwordField.text)
    sys.exit(0)
  }
} // end of class

object GameStoreGui {
  def main(args: Array[String]) {
    val ui = new UI
    ui.visible = true
  }
}
