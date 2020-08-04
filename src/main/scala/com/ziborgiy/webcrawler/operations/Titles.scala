package com.ziborgiy.webcrawler.operations

import cats.effect.Sync
import cats.implicits._
import org.jsoup.Jsoup

import scala.util.Try

trait Titles[F[_]] {
  def getTitles(vector: Vector[String]): F[Map[String, String]]
}

object Titles {
  def apply[F[_]](implicit ev: Titles[F]): Titles[F] = ev


  def impl[F[_] : Sync]: Titles[F] = new Titles[F] {

    def getTitles(urls: Vector[String]) = {
      val unique = urls.toSet.toVector
      unique.zip(unique.map(getTitle)).toMap.pure[F]
    }

    private[this] def getTitle(url: String) = Try(Jsoup.connect(url).get).toEither match {
      case Left(value) => s"${value.getMessage}"
      case Right(value) => value.title()
    }
  }
}
