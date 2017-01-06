package $organization$.$name;format="lower,word"$

import org.http4s.server.ServerApp
import org.http4s.server.blaze.BlazeBuilder

object Main extends ServerApp {

  def server(args: List[String]) = BlazeBuilder.bindHttp(8080)
    .mountService(WebApp.service, "/")
    .start
}
