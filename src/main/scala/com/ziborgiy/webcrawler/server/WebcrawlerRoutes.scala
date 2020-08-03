package com.ziborgiy.webcrawler.server

import cats.effect.Sync
import cats.implicits._
import com.ziborgiy.webcrawler.model.Urls
import com.ziborgiy.webcrawler.model.Codecs._
import com.ziborgiy.webcrawler.operations.Titles
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object WebcrawlerRoutes {

  def crawlerRoutes[F[_]: Sync](T: Titles[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case rq@POST -> Root / "getTitles" =>
        for {
          req <- rq.as[Urls]
          titles <- T.getTitles(req.urls)
          resp <- Ok(titles)
        } yield resp
    }
  }

}
