package com.ziborgiy.webcrawler.operations

import cats.Parallel
import cats.effect.concurrent.Semaphore
import cats.effect.{Concurrent, Sync, Timer}
import cats.implicits._
import org.jsoup.Jsoup

import scala.util.Try

trait Titles[F[_]] {
  def getTitles(urls: Vector[String])(channel: Map[String, String]): F[Unit]
}

object Titles {

  def impl[F[_] : Sync : Concurrent : Timer : Parallel]: Titles[F] = new Titles[F] {

    def getTitles(urls: Vector[String])(channel: Map[String, String]): F[Unit] = {
      println()
      for {
        s <- Semaphore(1)
        _ <- urls.toSet.toVector.map((u: String) => new PreciousResource[F](u, updateChannel(channel), s).use).parSequence.void
      } yield ()
    }

    def getTitle(url: String): (String, String) = {
      println()
      Try(Jsoup.connect(url).get).toEither match {
        case Left(value) => url -> s"${value.getMessage}"
        case Right(value) => url -> value.title()
      }
    }

    def updateChannel(channel: Map[String, String])(url: String) = {
      (channel.toVector :+ getTitle(url)).toMap
    }
  }
}
