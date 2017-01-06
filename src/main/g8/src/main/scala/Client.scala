package $organization$.$name;format="lower,word"$

import org.http4s.client.blaze.PooledHttp1Client

import scalaz.concurrent.Task
import scalaz.{ -\/, \/- }

object Client {
  def main(args: Array[String]): Unit = {
    val httpClient = PooledHttp1Client()
    val helloScala: Task[Dog] = httpClient.expect[Dog]("http://localhost:8080/dogs")
    helloScala.unsafePerformSyncAttempt match {
      case \/-(d) ⇒ print(d)
      case -\/(e) ⇒ print(e.getLocalizedMessage)
    }
  }
}
