package com.ziborgiy.webcrawler

import cats.effect.IO
import org.http4s._
import org.http4s.circe._
import io.circe.literal._
import org.http4s.implicits._
import org.specs2.matcher.MatchResult
import org.http4s.client.dsl.io._

class CrawlerSpec extends org.specs2.mutable.Specification {

  "CrawlerSpec" >> {
    "return 404" >> {
      ret404()
    }
  }

  private[this] val t1: Response[IO] = {
    val rq = Method.POST(json"""{"urls":["http://google.com/"]}""", uri"localhost:8081/getTitles").unsafeRunSync()
    val uris = Uris.impl[IO]
    WebcrawlerRoutes.crawlerRoutes(uris).orNotFound(rq).unsafeRunSync()
  }

  private[this] def ret404(): MatchResult[Status] =
    t1.status must beEqualTo(Status.NotFound)

}