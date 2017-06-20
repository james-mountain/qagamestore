/**
	* Created by Administrator on 19/06/2017.
	*/
class Employee(eId: Int, eFullName: String, eEmail: String, eIsManager: Boolean, eAddress: String, eTel: String, ePass: String) extends Person {
	protected var isManager : Boolean = eIsManager
	protected var address : String = eAddress
	protected var tel : String = eTel
	protected var pass : String = ePass

	id = eId
	fullName = eFullName
	email = eEmail

	def getIsManager() : Boolean = { isManager }
	def setIsManager(isManager : Boolean) : Unit = { this.isManager = isManager }

	def getAddress() : String = {	address	}
	def setAddress(address : String) : Unit = { this.address = address }

	def getTel() : String = { tel }
	def setTel(tel : String) : Unit = { this.tel = tel }

	def getPass() : String = { pass }
	def setPass(pass : String): Unit = { this.pass = pass }

	override def toString() : String = {
		List(id.toString, fullName, email, isManager.toString, address, tel, pass).mkString(",")
	}
}
