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
    title = user.getFullName()
    visible = true
    contents = mainMenu()
    resizable = false
    centerOnScreen()
  }

  def mainMenu(): Component = {
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
        contents += logoutButton
        contents += Swing.HStrut(30)
        contents += closeButton
        contents += Swing.HStrut(30)
      }
      contents += Swing.VStrut(20)
    }
    contents
  }

  def delete(): Unit ={
    frame.dispose()
  }
}
