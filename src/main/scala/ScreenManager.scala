import scala.swing.MainFrame
/**
  * Created by Administrator on 22/06/2017.
  */
object ScreenManager extends App{
  var screen: MainFrame = null
  screen = new LoginUI

  def login(): Unit ={
    screen match {
      case menu: MainMenuUi => menu.delete()
      case till: TillGUI => till.delete()
    }
    screen = new LoginUI
  }

  def menu(user: Employee): Unit ={
    screen match {
      case login: LoginUI => login.delete()
      case till: TillGUI => till.delete()
    }
    screen = new MainMenuUi(user)
  }

  def till(user: Employee): Unit ={
    screen match {
      case login: LoginUI => login.delete()
      case menu: MainMenuUi => menu.delete()
    }
    screen = new TillGUI(user)
  }

  def close() {
    FileHandler.saveFiles()
    sys.exit(0)
  }

}
