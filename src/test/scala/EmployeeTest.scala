/**
	* Created by Administrator on 20/06/2017.
	*/

import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class EmployeeTest extends FunSuite{
	val employee = new Employee(0, "Gary", "Gary@gmail.com", true, "1 Long Road", "01234567899", "Password")

	test("A manager should be a manager"){
		assert(employee.getIsManager() === true)
	}

	test("A manager should be able to be demoted") {
		employee.setIsManager(false)
		assert(employee.getIsManager() === false)
	}

	test("An employee should have an address") {
		assert(employee.getAddress() === "1 Long Road")
	}

	test("An employee should be able to change their address") {
		employee.setAddress("2 Long Road")
		assert(employee.getAddress() === "2 Long Road")
	}

	test("An employee should have a telephone number") {
		assert(employee.getTel() === "01234567899")
	}

	test("An employee should be able to change their telephone number") {
		employee.setTel("0777777777")
		assert(employee.getTel() === "0777777777")
	}

	test("An employee should have a password") {
		assert(employee.getPass() === "Password")
	}

	test("An employee should be able to have their password changed") {
		employee.setPass("P4$$w0rd")
		assert(employee.getPass() === "P4$$w0rd")
	}

	/*"A person" should "have a manager value assigned to them" in {
		val employee = new Employee
		employee.getIsManager() should be (true)
	}
	it should "have a address" in {
		val employee = new Employee
		employee.getAddress() should be ("1 Long Road")
	}
	it should "have an telephone number" in {
		val employee = new Employee
		employee.getTel() should be("01234567899")
	}*/
}
