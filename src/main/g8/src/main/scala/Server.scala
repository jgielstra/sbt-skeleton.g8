package $organization$.$name;format="lower,word"$

import org.http4s._
import org.http4s.dsl._

import scala.util.{ Failure, Success, Try }
import scalaz.concurrent.Task

class ServiceRoutes(api: ServiceAPI) {
  val service =
    HttpService {
      case GET -> Root / "dogs" ⇒ api.getAllDogs
      case GET -> Root / "dogs" / offset ⇒ api.getDog(offset)
      case req@POST -> Root / "dogs" ⇒ api.addDog(req)
      case req@GET -> Root / "healthcheck" ⇒ Ok(ResponseMsg("OK")) // TODO: simple health check api.. should check things like connectivity: db, redis, kafka, etc..
    }

}
object ServiceRoutes{
  def apply(api: ServiceAPI): ServiceRoutes = new ServiceRoutes(api)
}

class ServiceAPI(store: SimpleStore[Dog]){
  def getAllDogs: Task[Response] = Ok(store.getAll)
    def addDog(req: Request): Task[Response] = {
    req.decode[Dog] { d ⇒ {
        store.put(d)
        Created(d)
      }
    }
  }

  def getDog(offsetStr: String): Task[Response] = {
    Try(offsetStr.toInt) match {
      case Success(id) ⇒
        store.get(id) match {
          case Some(d) ⇒ Ok(d)
          case None    ⇒ NotFound(ResponseMsg("No dog at index: "+id))
        }
      case Failure(_) ⇒ BadRequest(ResponseMsg(offsetStr + " not a valid integer"))
    }
  }
}
object ServiceAPI{
  def apply(store: SimpleStore[Dog]): ServiceAPI = new ServiceAPI(store)
}
