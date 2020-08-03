package com.ziborgiy.webcrawler

import cats.effect.{ConcurrentEffect, ContextShift, ExitCode, IO, Timer}

import scala.concurrent.ExecutionContext.global
import com.ziborgiy.webcrawler.server.WebcrawlerServer

object TestServer {
  implicit val cs: ContextShift[IO] = IO.contextShift(global)
  implicit val t: Timer[IO] = IO.timer(global)

  def server = {
    WebcrawlerServer.stream[IO].compile.drain.as(ExitCode.Success)
  }

}
