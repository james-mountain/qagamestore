/**
  * Created by Calin on 20/06/2017.
  */



import java.awt.Dimension

import scala.collection.mutable.ListBuffer
import scala.swing._
import BorderPanel.Position

class MainGUI extends MainFrame {
  title = "GUI Program #1"
  preferredSize = new Dimension(1024, 720)
  var panel= new Panel {}
  var textfield:TextField = new TextField("Username")

  val label:Label = new Label("Output")
  contents= new BorderPanel (){
    add(textfield,BorderPanel.Position.North)
    add(label,BorderPanel.Position.South)
  }
}

object GuiProgramOne {
  def main(args: Array[String]) {
    // val ui = new MainGUI
    // ui.visible = true
    val r=new Receipt(1,ListBuffer[Double](),0,ListBuffer[String](),"Cash")
    r.addItem(new Item(1,"First",10,15,150,"PC"),2)
   println(r.toString())

  }
}