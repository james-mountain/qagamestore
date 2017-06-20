/**
  * Created by James Mountain on 19/06/2017.
  */
object Main {
  def main(args: Array[String]): Unit = {
    println("Hello World")
    println("New line added")
    println("Brand new line")

    val person = new Employee(0, "Gary", "Gary@gmail.com", true, "1 Long Road", "01234567899", "Password")
    println(person.getIsManager())
  }
}
