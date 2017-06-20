import scala.swing._
import scala.swing.event._

class UI extends MainFrame {
  def restrictHeight(s: Component) {
    s.maximumSize = new Dimension(Short.MaxValue, s.preferredSize.height)
  }

  title = "Game Store"

  val emailField = new TextField {
    columns = 32
  }

  val passwordField = new TextField {
    columns = 32
  }

  restrictHeight(emailField)

  contents = new BoxPanel(Orientation.Vertical) {
    contents += new BoxPanel(Orientation.Horizontal) {
      contents += new Label("Email Address: ")
      contents += new Label("Password: ")
      contents += Swing.HStrut(5)
      contents += emailField
      contents += passwordField
    }
    contents += Swing.VStrut(5)
    contents += new BoxPanel(Orientation.Horizontal) {
      contents += Swing.HStrut(20)
    }
    contents += Swing.VStrut(5)
    contents += Swing.VStrut(3)
    contents += Swing.VStrut(5)
    contents += new BoxPanel(Orientation.Horizontal) {
      contents += Swing.HGlue
      contents += Button("Close") {
        reportAndClose()
      }
    }
    for (e <- contents)
      e.xLayoutAlignment = 0.0
    border = Swing.EmptyBorder(10, 10, 10, 10)
  }

  listenTo(emailField)
  listenTo(passwordField)

  reactions += {
    case EditDone(`emailField`) =>
      println("Your email is now: " + emailField.text)
    case EditDone(`passwordField`) =>
      println("Your password is now: " + passwordField.text)
    case ButtonClicked(s) =>
      println("Button click on button: '" + s.text + "'")
  }

  def reportAndClose() {
    println("Your email: " + emailField.text)
    println("Your password: " + passwordField.text)
    sys.exit(0)
  }
}

object GameStoreGui {
  def main(args: Array[String]) {
    val ui = new UI
    ui.visible = true
  }
}
