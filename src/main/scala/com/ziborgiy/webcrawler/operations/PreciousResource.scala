package com.ziborgiy.webcrawler.operations

import cats.effect.concurrent.Semaphore
import cats.effect.{Concurrent, Timer}
import cats.implicits._


class PreciousResource[F[_]](url: String, f: String => Map[String, String], s: Semaphore[F])(implicit F: Concurrent[F], timer: Timer[F]) {
  def use: F[Unit] = {
    for {
      x <- s.available
      _ <- F.delay(println(s"${
        f(url)
      } >> Availability: $x"))
      _ <- s.acquire
      y <- s.available
      _ <- F.delay(println(s"${
        f(url)
      } >> Started | Availability: $y"))
      _ <- s.release
      z <- s.available
      _ <- F.delay(println(s"${
        f(url)
      } >> Done | Availability: $z"))
    } yield ()
  }
}