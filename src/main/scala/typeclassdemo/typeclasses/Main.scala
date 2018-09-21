package typeclassdemo.typeclasses

object model {
  trait ToJson[A] {
    def toJson(a: A): String
  }

  object ToJson {
    def apply[A](implicit tc: ToJson[A]) = tc

    implicit def userInstance: ToJson[User] = new ToJson[User] {
      def toJson(u: User) = s"""{"id": ${u.id}, "name": "${u.name}"}"""
    }

    trait ToJsonOps[A] {
      def self: A
      def typeclass: ToJson[A]

      def toJson = typeclass.toJson(self)
    }

    object syntax {
      implicit def produceToJsonOps[A: ToJson](x: A): ToJsonOps[A] =
        new ToJsonOps[A] {
          def self = x
          def typeclass = ToJson[A]
        }
    }
  }

  case class User(id: Int, name: String)
}
import model._, ToJson.syntax._

trait Server {
  def respond[A: ToJson](x: A): Unit =
    println(x.toJson)
}

object SingleUser extends App with Server {
  val user = User(1, "foo")
  respond(user)
}

object MultipleUsers extends App with Server {
  implicit def listToJson[A: ToJson]: ToJson[List[A]] =
    new ToJson[List[A]] {
      def toJson(l: List[A]) = s"""[${l.map(_.toJson).mkString(",")}]"""
    }


  val allUsers = List(
    User(1, "foo")
  , User(2, "bar")
  , User(3, "x"  ))

  respond(allUsers)
}