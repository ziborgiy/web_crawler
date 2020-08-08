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
          ch <- (Map.empty[String,String]).pure[F]
          req <- rq.as[Urls]
          _ <- T.getTitles(req.urls)(ch)
          resp <- Ok(ch)
        } yield resp
    }
  }

}
