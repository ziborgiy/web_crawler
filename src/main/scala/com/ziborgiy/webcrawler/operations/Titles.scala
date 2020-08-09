package com.ziborgiy.webcrawler.operations

import cats.Parallel
import cats.data.NonEmptyList
import cats.effect.{Concurrent, Sync}
import cats.implicits._
import org.jsoup.Jsoup

import scala.util.Try

trait Titles[F[_]] {
  def getTitles(urls: Vector[String]): F[Map[String, String]]
}

object Titles {

  def impl[F[_] : Sync : Parallel](implicit c: Concurrent[F]): Titles[F] = new Titles[F] {

    def getTitles(urls: Vector[String]): F[Map[String, String]] = {
      for {
        nel <- NonEmptyList.fromList(urls.toSet.toList).getOrElse(None.orNull).parTraverse(getTitle)
      } yield {
        nel.toList.toMap
      }
    }

    def getTitle(url: String): F[(String, String)] = c.delay {
      Try(Jsoup.connect(url).ignoreHttpErrors(true).get).toEither match {
        case Left(value) => url -> s"${value.getMessage}"
        case Right(value) => url -> value.title()
      }
    }
  }
}


