package typeclassdemo.wrappers

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
  def respond[A](x: A)(implicit c: A => ToJson): Unit =
    println(x.toJson)
}

object SingleUser extends App with Server {
  val user = User(1, "foo")
  respond(user)
}

object MultipleUsers extends App with Server {
  implicit def listToJson[A](l: List[A])(implicit c: A => ToJson) =
    new ToJson {
      def toJson = s"""[${l.map(_.toJson).mkString(",")}]"""
    }


  val allUsers = List(
    User(1, "foo")
  , User(2, "bar")
  , User(3, "x"  ))

  respond(allUsers)
}