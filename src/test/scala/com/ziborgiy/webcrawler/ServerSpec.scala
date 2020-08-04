package com.ziborgiy.webcrawler

import cats.effect.{ConcurrentEffect, ContextShift, IO, Timer}
import com.ziborgiy.webcrawler.server.WebcrawlerServer
import io.circe.Json
import io.circe.literal._
import org.http4s.Method
import org.http4s.circe._
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.client.dsl.io._
import org.http4s.implicits._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.ExecutionContext.global

class ServerSpec extends AnyFlatSpec with Matchers {
  implicit val t: Timer[IO] = IO.timer(global)
  implicit val cs: ContextShift[IO] = IO.contextShift(global)
  implicit val ce: ConcurrentEffect[IO] = IO.ioConcurrentEffect
  WebcrawlerServer.stream[IO].compile.drain.start.unsafeRunSync()

  private val BASIC_JSON = json"""{"urls":["http://google.com/"]}"""
  private val URI = uri"http://localhost:8081/getTitles"

  private def rq =
    BlazeClientBuilder[IO](global).resource.use(_.expect[Json](Method.POST(BASIC_JSON, URI).unsafeRunSync()))

  it should "check server working" in {
    noException shouldBe thrownBy(rq)
  }

}
