/**
	* Created by Administrator on 19/06/2017.
	*/
import org.scalatest._

class PersonTest extends FlatSpec with Matchers {
	"A person" should "have an id assigned to them" in {
		val person = new Employee
		person.getId() should be (0)
	}
	it should "have a full name" in {
		val person = new Employee
		person.getFullName() should be ("Gary")
	}
	it should "have an email address" in {
		val person = new Employee
		person.getEmail() should be ("Gary@gmail.com")
	}

}
