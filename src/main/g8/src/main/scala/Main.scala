package $organization$.$name;format="lower,word"$

import org.http4s.server.ServerApp
import org.http4s.server.blaze.BlazeBuilder

object Main extends ServerApp {

  def server(args: List[String]) = {

    val store = new SimpleStore[Dog]
    store.putAll( List(
      Dog("Cerberus", 5, "Mastiff"),
      Dog("Fluffy", 5, "Poodle")
    ))
    val api = ServiceAPI(store)
    val routes = ServiceRoutes(api)

    BlazeBuilder.bindHttp(8080)
      .mountService(routes.service, "/")
      .start
  }
}
