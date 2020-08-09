package com.ziborgiy.webcrawler.server

import cats.effect.{Concurrent, Sync}
import cats.effect.concurrent.MVar
import cats.implicits._
import com.ziborgiy.webcrawler.model.Urls
import com.ziborgiy.webcrawler.model.Codecs._
import com.ziborgiy.webcrawler.operations.Titles
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object WebcrawlerRoutes {

  def crawlerRoutes[F[_]: Sync : Concurrent](T: Titles[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case rq@POST -> Root / "getTitles" =>
        for {
          req <- rq.as[Urls]
          res <- T.getTitles(req.urls)
          resp <- Ok(res)
        } yield resp
    }
  }

}
