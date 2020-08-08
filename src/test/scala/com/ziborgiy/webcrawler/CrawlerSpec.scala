package com.ziborgiy.webcrawler

import cats.effect.IO
import cats.implicits._
import com.ziborgiy.webcrawler.operations.Titles
import com.ziborgiy.webcrawler.server.WebcrawlerRoutes
import io.circe.Json
import io.circe.literal._
import org.http4s._
import org.http4s.circe._
import org.http4s.client.dsl.io._
import org.http4s.implicits._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CrawlerSpec extends AnyFlatSpec with Matchers {
/*
  val success: Titles[IO] = (vector: Vector[String]) => Map("http://google.com" -> "Google").pure[IO]
  val response: IO[Response[IO]] = WebcrawlerRoutes.crawlerRoutes[IO](success).orNotFound.run(
    Method.POST(json"""{"urls":["http://google.com/"]}""", uri"/getTitles").unsafeRunSync()
  )
  val expectedJson = Json.obj(
    ("http://google.com", Json.fromString("Google")),
  )

  it should "check[Json] return true" in {
    check[Json](response, Status.Ok, Some(expectedJson)) should be(true)
  }

  private def check[A](
                actual: IO[Response[IO]],
                expectedStatus: Status,
                expectedBody: Option[A]
              )(
                implicit ev: EntityDecoder[IO, A]
              ): Boolean = {
    val actualResp = actual.unsafeRunSync()
    val statusCheck = actualResp.status == expectedStatus
    val bodyCheck = expectedBody.fold[Boolean](
      actualResp.body.compile.toVector.unsafeRunSync.isEmpty)(
      expected => actualResp.as[A].unsafeRunSync() == expected
    )
    statusCheck && bodyCheck
  }*/
}