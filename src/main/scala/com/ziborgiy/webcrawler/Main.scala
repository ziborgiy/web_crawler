package com.ziborgiy.webcrawler

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._

object Main extends IOApp {
  def run(args: List[String]) =
    WebcrawlerServer.stream[IO].compile.drain.as(ExitCode.Success)
}