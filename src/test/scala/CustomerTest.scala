/**
	* Created by Administrator on 20/06/2017.
	*/
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

import scala.collection.mutable.ListBuffer

@RunWith(classOf[JUnitRunner])
class CustomerTest extends FunSuite{
	val customer = new Customer(0, "Gary", "Gary@gmail.com", 0)

	test("A customer should have no membership points") {
		assert(customer.getMembershipPoints() === 0)
	}

	test("A customer should be able to gain membership points"){
		customer.addMemebershipPoints(50)
		assert(customer.getMembershipPoints() === 50)
	}

	test("A customer should be able to remove membership points"){
		var pastPoints = customer.getMembershipPoints()
		var pointsToRemove = 50;
		customer.removeMembershipPoints(pointsToRemove)
		assert(customer.getMembershipPoints() === pastPoints - pointsToRemove)
	}

	test("A customer should be able to add a preorder"){
		var itemId : Int = 0
		var prevSize: Int = customer.getPreOrders().size
		customer.addPreOrder(itemId)
		assert(customer.getPreOrders().size === prevSize+1 )
	}

	test("A customer should be able to remove a preorder") {
		var prevSize: Int = customer.getPreOrders().size
		customer.removePreOrder(0)
		assert(customer.getPreOrders().size === prevSize-1)
	}
}
