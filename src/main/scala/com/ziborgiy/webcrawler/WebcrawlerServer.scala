package com.ziborgiy.webcrawler

import cats.effect.{ConcurrentEffect, ContextShift, Timer}
import fs2.Stream
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger
import scala.concurrent.ExecutionContext.global

object WebcrawlerServer {
  def stream[F[_]: ConcurrentEffect](implicit T: Timer[F], C: ContextShift[F]): Stream[F, Nothing] = {
    val crawlAlg = Uris.impl[F]
    val httpApp = WebcrawlerRoutes.crawlerRoutes[F](crawlAlg).orNotFound
    val finalHttpApp = Logger.httpApp(true, true)(httpApp)
    for {
      exitCode <- BlazeServerBuilder[F](global)
        .bindHttp(8081, "0.0.0.0")
        .withHttpApp(finalHttpApp)
        .serve
    } yield exitCode
  }.drain
}
