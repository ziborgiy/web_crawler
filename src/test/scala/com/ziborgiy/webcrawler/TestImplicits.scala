package com.ziborgiy.webcrawler

import cats.Parallel.Aux
import cats.effect
import cats.effect.{ConcurrentEffect, ContextShift, IO, Timer}

import scala.concurrent.ExecutionContext.global

object TestImplicits {
  implicit val t: Timer[IO] = IO.timer(global)
  implicit val cs: ContextShift[IO] = IO.contextShift(global)
  implicit val ce: ConcurrentEffect[IO] = IO.ioConcurrentEffect
  implicit val p: Aux[IO, effect.IO.Par] = IO.ioParallel


}
