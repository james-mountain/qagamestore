/**
  * Created by Administrator on 19/06/2017.
  */
import java.time.LocalDate
class Game(i_id : Int, itemName: String, itemStockPrice : Double, sPrice: Double, itemStock : Int, iType : String, rDate : LocalDate, gameRating : String)
  extends Item(i_id, itemName, itemStockPrice, sPrice, itemStock, iType) {
  val releaseDate = rDate
  val rating = gameRating

  def getReleaseDate() = releaseDate
  def getRating() = rating

  def getReleased() : Boolean = {
    releaseDate.isBefore(LocalDate.now())
  }

  override def toString: String = List(id.toString, name, stockPrice.toString, salePrice.toString, stock.toString, itemtype, releaseDate.toString, gameRating).mkString(",")
}
