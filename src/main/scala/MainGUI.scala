/**
  * Created by Calin on 20/06/2017.
  */

import java.lang._
import scala.swing._

class MainGUI extends MainFrame {
  title = "GUI Program #1"
  preferredSize = new Dimension(320, 240)
  contents = new Label("Here is the contents!")
}

object GuiProgramOne {
  def main(args: Array[String]) {
    val ui = new MainGUI
    ui.visible = true
    println("End of main function")
  }
}