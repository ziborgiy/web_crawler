package com.ziborgiy.webcrawler

import cats.effect.Sync
import cats.implicits._
import com.ziborgiy.webcrawler.Uris.Urls
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.circe._

object WebcrawlerRoutes {

  def crawlerRoutes[F[_]: Sync](U: Uris[F]): HttpRoutes[F] = {
    implicit val mapEncoder = jsonEncoderOf[F, Map[String,String]]
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case rq@POST -> Root / "getTitles" =>
        for {
          req <- rq.as[Urls]
          titles <- U.getTitles(req.urls)
          resp <- Ok(titles)
        } yield resp
    }
  }

}