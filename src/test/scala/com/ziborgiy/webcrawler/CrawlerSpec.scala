package com.ziborgiy.webcrawler

import cats.effect.{ContextShift, IO}
import com.ziborgiy.webcrawler.operations.Titles
import com.ziborgiy.webcrawler.server.WebcrawlerRoutes
import org.http4s._
import org.http4s.circe._
import io.circe.literal._
import org.http4s.implicits._
import org.http4s.client.dsl.io._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import cats.implicits._
import io.circe.Json
import org.scalatest.BeforeAndAfterAll

import scala.concurrent.ExecutionContext.global

class CrawlerSpec extends AnyFlatSpec with Matchers with BeforeAndAfterAll {

  implicit val cs: ContextShift[IO] = IO.contextShift(global)

  val SERVER = TestServer.server.start.unsafeRunSync()
  override def afterAll() = {
        SERVER.cancel
  }

  it should "check[Json] return true" in {
    t1
    check[Json](response,Status.Ok,Some(expectedJson)) should be (true)
  }

  def check[A](
                actual: IO[Response[IO]],
                expectedStatus: Status,
                expectedBody: Option[A]
              ) (
              implicit ev: EntityDecoder[IO, A]
  ): Boolean = {
    val actualResp = actual.unsafeRunSync()
    val statusCheck = actualResp.status == expectedStatus
    val bodyCheck = expectedBody.fold[Boolean](
      actualResp.body.compile.toVector.unsafeRunSync.isEmpty)(
      expected => actualResp.as[A].unsafeRunSync() == expected
    )
    statusCheck && bodyCheck
  }

  private[this] val t1: Response[IO] = {
    val rq = Method.POST(json"""{"urls":["http://google.com/"]}""", uri"localhost:8081/getTitles").unsafeRunSync()
    val titles = Titles.impl[IO]
    WebcrawlerRoutes.crawlerRoutes[IO](titles).orNotFound.run(rq).unsafeRunSync()
  }

  val success: Titles[IO] = (vector: Vector[String]) => Map("http://google.com" -> "Google").pure[IO]
  val response: IO[Response[IO]] = WebcrawlerRoutes.crawlerRoutes[IO](success).orNotFound.run(
    Method.POST(json"""{"urls":["http://google.com/"]}""", uri"/getTitles").unsafeRunSync()
  )

  val expectedJson = Json.obj(
    ("http://google.com", Json.fromString("Google")),
  )




}