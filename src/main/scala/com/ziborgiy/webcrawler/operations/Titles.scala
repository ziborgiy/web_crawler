package com.ziborgiy.webcrawler.operations

import cats.effect.{Concurrent, Sync}
import cats.implicits._
import org.jsoup.Jsoup

import scala.util.Try

trait Titles[F[_]] {
  def getTitles(urls: Vector[String]): F[List[F[(String, String)]]]
}

object Titles {

  def impl[F[_] : Sync : Concurrent]: Titles[F] = new Titles[F] {

    def getTitles(urls: Vector[String]) = {
      urls.toList.map(getTitle).pure[F]
    }

    def getTitle(url: String) = {
      Try(Jsoup.connect(url).get).toEither match {
        case Left(value) => url -> s"${value.getMessage}"
        case Right(value) => url -> value.title()
      }
    }.pure[F]
  }
}

//

