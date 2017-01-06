package $organization$.$name;format="lower,word"$

import argonaut.Argonaut._
import argonaut._
import org.http4s.argonaut._
import org.http4s.{ EntityDecoder, EntityEncoder }

final case class Dog(name: String, age: Int, breed: String)

object Dog {
  implicit def DogCodec: CodecJson[Dog] = casecodec3(Dog.apply, Dog.unapply)("name", "age", "breed")

  implicit val dogEntityDecoder: EntityDecoder[Dog] = jsonOf[Dog]
  implicit val dogEntityEncoder: EntityEncoder[Dog] = jsonEncoderOf[Dog]
  implicit val dogListEntityEncoder: EntityEncoder[List[Dog]] = jsonEncoderOf[List[Dog]]

}

final case class ResponseMsg(msg: String)
object ResponseMsg {
  implicit val responseCodecJson: CodecJson[ResponseMsg] =
    Argonaut.casecodec1(ResponseMsg.apply, ResponseMsg.unapply)("msg")
  implicit val responseEntityDecoder: EntityDecoder[ResponseMsg] =
    jsonOf[ResponseMsg]
  implicit val responseEntityEncoder: EntityEncoder[ResponseMsg] =
    jsonEncoderOf[ResponseMsg]
}