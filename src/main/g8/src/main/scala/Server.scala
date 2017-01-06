package $organization$.$name;format="lower,word"$

import org.http4s._
import org.http4s.dsl._

import scala.util.{ Failure, Success, Try }
import scalaz.concurrent.Task

object WebApp {
  val dogs = List(
    Dog("Cerberus", 5, "Mastiff"),
    Dog("Fluffy", 5, "Poodle")
  )
  val service =
    HttpService {
      case GET -> Root / "dogs"               ⇒ Ok(dogs)
      case GET -> Root / "dogs" / offset      ⇒ getDog(offset)
      case req @ POST -> Root / "dogs"        ⇒ postScala(req)
      case req @ GET -> Root / "healthcheck" ⇒ Ok(ResponseMsg("OK")) // TODO: simple health check api.. should check things like connectivity: db, redis, kafka, etc..
    }
  def postScala(req: Request): Task[Response] = {
    req.decode[Dog] { d ⇒ Ok(d) }
  }

  def getDog(offsetStr: String): Task[Response] = {
    Try(offsetStr.toInt) match {
      case Success(i) ⇒
        dogs.lift(i) match {
          case Some(d) ⇒ Ok(d)
          case None    ⇒ NotFound(ResponseMsg("No dog at index: "+1))
        }
      case Failure(_) ⇒ BadRequest(ResponseMsg(offsetStr + " not a valid integer"))
    }
  }
}