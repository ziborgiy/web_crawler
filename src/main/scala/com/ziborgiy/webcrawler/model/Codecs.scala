package com.ziborgiy.webcrawler.model

import cats.effect.Sync
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import org.http4s.EntityDecoder
import org.http4s.circe.jsonEncoderOf
import org.http4s.circe.jsonOf

object Codecs {
  // $COVERAGE-OFF$
  implicit val urlDecoder: Decoder[Urls] = deriveDecoder[Urls]

  implicit def urlEntityDecoder[F[_] : Sync]: EntityDecoder[F, Urls] =
    jsonOf

  implicit def mapEncoder[F[_] : Sync] = jsonEncoderOf[F, Map[String,String]]
  // $COVERAGE-ON$
}
