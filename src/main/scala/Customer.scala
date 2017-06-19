import scala.collection.mutable.ListBuffer

/**
	* Created by Administrator on 19/06/2017.
	*/
class DummyItems
{

}

class Customer extends Person{
	def getMembershipPoints() : Int = {
		0
	}
	def setMembershipPoints(membershipPoints : Int) : Unit = {

	}
	def getPreOrders() : ListBuffer[DummyItems] = {
		new ListBuffer[DummyItems]
	}
	def setPreOrders(preOrders : ListBuffer[DummyItems]) : Unit = {

	}
}
