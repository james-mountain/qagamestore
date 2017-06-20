import java.io.{File, PrintWriter}
import java.time.LocalDate

import scala.collection.mutable.ListBuffer
import scala.io.Source

/**
  * Created by Administrator on 20/06/2017.
  */
object FileHandler {
  def loadFiles(gameStore : GameStore): Unit = {
    loadItems(gameStore)
    loadCustomer(gameStore)
  }

  def loadItems(gameStore : GameStore) = {
    val gameFileItems = Source.fromFile("C:\\Users\\Administrator\\Desktop\\games").getLines().toList
    val nonGameItems = Source.fromFile("C:\\Users\\Administrator\\Desktop\\items").getLines().toList
    val gameFileStrings = gameFileItems.map(str => str.split(","))
    val nonGamefileStrings = nonGameItems.map(str => str.split(","))

    // game items
    for (line <- gameFileStrings) {
      val newItem = new Game(line(0).toInt, line(1), line(2).toDouble, line(3).toDouble, line(4).toInt, line(5), LocalDate.parse(line(6)), line(7))
      gameStore.addItem(newItem)
    }

    // non game items
    for (line <- nonGamefileStrings) {
      val newItem = new Item(line(0).toInt, line(1), line(2).toDouble, line(3).toDouble, line(4).toInt, line(5))
      gameStore.addItem(newItem)
    }
  }

  def loadCustomer(gameStore : GameStore) = {
    val customerRecords = Source.fromFile("C:\\Users\\Administrator\\Desktop\\Customers").getLines().toList
    val customerStrings = customerRecords.map(str => str.split(","))

    for(line <- customerStrings) {
      val newCustomer = new Customer(line(0).toInt, line(1), line(2), line(3).toInt)
      println(line(4))
      val preOrderString = line(4).split('|').toList
      for (order <- preOrderString) {
        newCustomer.addPreOrder(order.toInt)
      }
    }
  }

  def saveFiles(gameStore : GameStore) = {
    saveItems(gameStore)
    saveCustomer(gameStore)
  }

  def saveItems(gameStore : GameStore): Unit = {
    val games = ListBuffer.empty[Game]
    val items = ListBuffer.empty[Item]

    gameStore.getItems().foreach {
      case item: Game => games += item
      case item: Item => items += item
    }

    val gamesWriter = new PrintWriter(new File("C:\\Users\\Administrator\\Desktop\\games"))
    games.foreach(game => gamesWriter.println(game.toString))
    gamesWriter.close()

    val itemsWriter = new PrintWriter(new File("C:\\Users\\Administrator\\Desktop\\items"))
    items.foreach(item => itemsWriter.println(item.toString))
    itemsWriter.close()
  }

  def saveCustomer(gameStore : GameStore) : Unit = {
    val customers = ListBuffer.empty[Customer]

    gameStore.getCustomers().foreach {
      case customer: Customer => customers += customer
    }

    val customerWriter = new PrintWriter(new File("C:\\Users\\Administrator\\Desktop\\customers"))
    customers.foreach(customer => customerWriter.println(customer.toString))
    customerWriter.close()
  }
}
