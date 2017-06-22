import java.awt.Dimension
import scala.swing.{PasswordField, _}
import scala.swing.event._
import scala.swing.{BoxPanel, Button, ButtonGroup, ComboBox, Dialog, Label, MainFrame, Orientation, RadioButton, ScrollPane, Separator, Swing, Table, TextField}
/**
  * Created by Administrator on 22/06/2017.
  */
class MainMenuUi(user: Employee) extends MainFrame {
  var adminButton = Button("Update Records") {
    maximumSize = new Dimension(200, 175)
  }
  var staffButton = Button("Tills") {
    maximumSize = new Dimension(200, 175)
    ScreenManager.till(user)
  }
  var logoutButton = Button("Logout"){
    ScreenManager.login()
  }

  var closeButton = Button("Close"){
    ScreenManager.close()
  }

  var frame = new Frame() {
    preferredSize = new Dimension(300, 250)
    title = user.getFullName()
    visible = true
    contents = mainMenu()
    centerOnScreen()
    resizable = false
  }

  def mainMenu(): Component = {
    val contents = new BoxPanel(Orientation.Vertical) {
      contents += new GridPanel(4,1) {
        contents += adminButton
        contents += staffButton
        contents += logoutButton
        contents += closeButton
      }
    }
    contents
  }

  def delete(): Unit ={
    frame.dispose()
  }
}
