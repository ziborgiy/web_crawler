package com.ziborgiy.webcrawler

import cats.effect.Sync
import cats.implicits._
import io.circe.Decoder
import io.circe.generic.semiauto._
import org.http4s.EntityDecoder
import org.http4s.circe._
import org.jsoup.Jsoup

import scala.util.Try

trait Uris[F[_]]{
  def getTitles(vector: Vector[String]): F[Map[String,String]]
}

object Uris {
  def apply[F[_]](implicit ev: Uris[F]): Uris[F] = ev

  final case class Urls(urls: Vector[String]) extends AnyVal
  object Urls {
    implicit val urlDecoder: Decoder[Urls] = deriveDecoder[Urls]
    implicit def urlEntityDecoder[F[_]: Sync]: EntityDecoder[F, Urls] =
      jsonOf
  }

  def impl[F[_]: Sync]: Uris[F] = new Uris[F]{
    def getTitles(urls: Vector[String]) = {
      val dest = urls.map(getTitle)
      val res = urls zip dest
      res.toMap.pure[F]
    }
    private[this] def getTitle(url: String) = {
      Try(Jsoup.connect(url).get).toEither match {
        case Left(value) => s"${value.getMessage.take(32)}..."
        case Right(value) => value.title()
      }
    }
  }
}