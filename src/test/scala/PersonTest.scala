/**
	* Created by Administrator on 19/06/2017.
	*/
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PersonTest extends FunSuite {
	val person = new Employee(0, "Gary", "Gary@gmail.com", true, "1 Long Road", "01234567899", "Password")

	test("A person should have an id"){
		assert(person.getId() === 0)
	}

	test("A person should have a name") {
		assert(person.getFullName() === "Gary")
	}

	test("A person should be able to change their name") {
		person.setFullName("Bob")
		assert(person.getFullName() === "Bob")
	}

	test("A person should have an email") {
		assert(person.getEmail() === "Gary@gmail.com")
	}

	test("A person should be able to change their email") {
		person.setEmail("Bob@gmail.com")
		assert(person.getEmail() === "Bob@gmail.com")
	}

}
