/**
	* Created by Alex Rimmer on 19/06/2017.
	*/

trait Person {
	protected var id : Int = 0
	protected var fullName : String = ""
	protected var email : String = ""

	def getId() : Int = {	id	}
	def setId(id : Int) : Unit = { this.id = id	}
	def getFullName() : String = { fullName	}
	def setFullName(fullName: String) : Unit = { this.fullName = fullName 	}
	def getEmail() : String = {	email	}
	def setEmail(email : String) : Unit = {	this.email = email }
}
