package Models

class Customer(private val id: Int, private var name: String) {

  // Getters
  def getId: Int = this.id

  def getName: String = this.name


  // Setters
  def setName(name: String): Unit = {
    this.name = name
  }


  // toString
  override def toString: String = s"Customer($id, $name)"

  def updateInfo(name: String, email: String): Unit = {
    try {
      setName(name)
    } catch {
      case e: Exception =>
        println(s"Error updating customer info: $e")
    }
  }

  def copyWithNewInfo(name: String): Customer = {
    try {
      val newCustomer = new Customer(this.id, name)
      newCustomer
    } catch {
      case e: Exception =>
        println(s"Error creating customer copy: $e")
        this
    }
  }
}

object Customer {
  def apply(id: Int, name: String): Customer = new Customer(id, name)
}
