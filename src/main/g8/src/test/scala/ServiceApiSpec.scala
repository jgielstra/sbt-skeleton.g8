package $organization$.$name;format="lower,word"$

import java.nio.charset.StandardCharsets

import org.http4s.{EntityBody, Request, Response, Status}
import org.scalatest._

import scalaz.stream.{Process, Process1}
import scalaz.stream.process1._
import _root_.argonaut._
import argonaut.Argonaut._
import scodec.bits.ByteVector

import scalaz.syntax.applicative._
import scalaz.std.list._
import scalaz.concurrent.Task

class ServiceApiSpec extends FlatSpec with Matchers {
  val store = new SimpleStore[Dog]()
  store.putAll(createSampleData)
  val api = ServiceAPI(store)

  // Helpers
  // Create some test data
  def createSampleData: List[Dog] = {
    List(Dog("Cerberus", 100, "Hound"),
      Dog("Fluffy", 4, "Poodle")
    )
  }

  def encodeJson[T](i:T)(implicit encode: EncodeJson[T]): EntityBody = {
    implicit val charSet = StandardCharsets.UTF_8
    type P[A] = Process[Task, A]
      ByteVector
        .encodeString(i.asJson.nospaces)
        .fold(Process fail _, _.pure[P])
  }
  //  parse responseBody
  def parse[T](req: Response)(implicit decode: DecodeJson[T]): Option[T] = {
    def parseBody[A](implicit decode: DecodeJson[A]): Process1[String, Option[A]] =
      lift(s => Parse.decodeOption[A](s))

    (req.bodyAsText |> parseBody[T]).runLast.unsafePerformSync.get
  }

  "getAllDogs" should "return a list of dogs and sample data" in {
    val response = api.getAllDogs.unsafePerformSync
    response.status shouldEqual Status.Ok
    parse[List[Dog]](response) match {
      case Some(dogs) => dogs should have length (2) //Ok have sample data..
      case None => fail("Bad response")
    }
  }
  "addDog" should "create a dog" in {
    val dog = Dog("Spike",2,"PitDot")
    val req = new Request(body = encodeJson[Dog](dog))
    val response = api.addDog(req).unsafePerformSync
    response.status shouldEqual Status.Created
    parse[Dog](response) match {
      case Some(dog) => dog.name shouldEqual  "Spike"
      case None => fail("Bad response")
    }
  }

  "getDog" should "return a dog" in {
    val response = api.getDog("0").unsafePerformSync
    response.status shouldEqual Status.Ok
    parse[Dog](response) match {
      case Some(dog) => dog.name shouldEqual  "Cerberus"
      case None => fail("Bad response")
    }
  }
  it should "return BadRequest" in {
    val response = api.getDog("derp").unsafePerformSync
    response.status shouldEqual Status.BadRequest
  }
  it should "return NotFound" in {
    val response = api.getDog("999").unsafePerformSync
    response.status shouldEqual Status.NotFound
  }
}
