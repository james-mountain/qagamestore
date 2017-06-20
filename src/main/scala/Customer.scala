import scala.collection.mutable.ListBuffer

/**
	* Created by Administrator on 19/06/2017.
	*/

class Customer(eId: Int, eFullName: String, eEmail: String, eMembershipPoints : Int) extends Person{

	protected var membershipPoints : Int = eMembershipPoints
	protected var preOrders : ListBuffer[Int] = new ListBuffer[Int]

	id = eId
	fullName = eFullName
	email = eEmail

	def getMembershipPoints() : Int = {	membershipPoints }
	def addMemebershipPoints(points : Int) : Unit = {	membershipPoints += points }
	def removeMembershipPoints(points : Int) : Unit = {	membershipPoints -= points }
	def getPreOrders() : ListBuffer[Int] = { preOrders }
	def addPreOrder(id : Int) : Unit = { preOrders += id }
	def removePreOrder(id : Int) : Unit = {
		def iter(index : Int) : Unit = index match {
			case a if index == preOrders.size => //do nothing
			case b if preOrders(index) == id => preOrders.remove(index)
			case _ => iter(index+1)
		}
		iter(0)
	}
	override def toString() : String = {
		List(id.toString, fullName, email, membershipPoints.toString, preOrders.mkString("|")).mkString(",")
	}
}
