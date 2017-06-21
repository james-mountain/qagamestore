import javax.swing.border.EmptyBorder

import scala.swing._
import scala.swing.event._

class MainMenu extends MainFrame {
  title = "Main Menu"

  val adminButton = new Button("Admin"){
//    maximumSize = new Dimension(100,70)
  }
  val staffButton = new Button("Till"){
//    maximumSize = new Dimension(100,70)
  }
//  peer.setLocationRelativeTo(null)
  contents = new GridPanel(2,2) {
    preferredSize = new Dimension(800, 500)
    contents += new GridPanel(1,2){
      contents += adminButton
      contents += staffButton
    }
    contents += new BoxPanel(Orientation.Horizontal){
      contents += new Button("Logout"){
      }

      contents += new Button("Close")
    }


  }
}

object MainMenuGui {
  def main(args: Array[String]): Unit = {
    val mainUi = new MainMenu
    mainUi.visible = true
  }
}

