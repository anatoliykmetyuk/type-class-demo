package typeclassdemo.traits

object model {
  trait ToJson {
    def toJson: String
  }

  case class User(id: Int, name: String) extends ToJson {
    def toJson = s"""{"id": $id, "name": "$name"}"""
  }
}
import model._

trait Server {
  def respond(x: ToJson): Unit =
    println(x.toJson)
}

object SingleUser extends App with Server {
  val user = User(1, "foo")
  respond(user)
}

object MultipleUsers extends App with Server {
  val allUsers = List(
    User(1, "foo")
  , User(2, "bar")
  , User(3, "x"  ))

  // respond(allUsers)
  //   type mismatch;
  // [error]  found   : List[typeclassdemo.traits.model.User]
  // [error]  required: typeclassdemo.traits.model.ToJson
  // [error]   respond(allUsers)
}