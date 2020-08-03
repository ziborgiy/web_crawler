package com.ziborgiy.webcrawler

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import com.ziborgiy.webcrawler.server.WebcrawlerServer

object Main extends IOApp {
  // $COVERAGE-OFF$
  def run(args: List[String]) =
    WebcrawlerServer.stream[IO].compile.drain.as(ExitCode.Success)
  // $COVERAGE-ON$

}