package com.ziborgiy.webcrawler.server

import cats.Parallel
import cats.data.NonEmptyList
import cats.effect.{Concurrent, Sync}
import cats.implicits._
import com.ziborgiy.webcrawler.model.Urls
import com.ziborgiy.webcrawler.model.Codecs._
import com.ziborgiy.webcrawler.operations.Titles
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import cats.effect.implicits.toConcurrentOps


object WebcrawlerRoutes {

  def crawlerRoutes[F[_]: Sync : Concurrent : Parallel](T: Titles[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case rq@POST -> Root / "getTitles" =>
        for {
          req <- rq.as[Urls]
          ios <- T.getTitles(req.urls)
          fibers <- NonEmptyList.fromList(ios).getOrElse(None.orNull).parTraverse(_.start)
          results <- fibers.parTraverse(_.join)
          resp <- Ok(results.toList.toMap)
        } yield resp
    }
  }

}
