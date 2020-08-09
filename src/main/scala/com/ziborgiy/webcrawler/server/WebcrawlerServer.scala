package com.ziborgiy.webcrawler.server

import cats.Parallel
import cats.effect.concurrent.MVar
import cats.effect.{ConcurrentEffect, ContextShift, Timer}
import com.ziborgiy.webcrawler.operations.Titles
import fs2.Stream
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger

import scala.concurrent.ExecutionContext.global

object WebcrawlerServer {
  def stream[F[_] : ConcurrentEffect: Parallel](implicit T: Timer[F], C: ContextShift[F]): Stream[F, Nothing] = {
    val crawlAlg = Titles.impl[F]
    val httpApp = WebcrawlerRoutes.crawlerRoutes[F](crawlAlg).orNotFound
    val finalHttpApp = Logger.httpApp(true, true)(httpApp)
    for {
      exitCode <- BlazeServerBuilder[F](global)
        .bindLocal(8081)
        .withHttpApp(finalHttpApp)
        .serve
    } yield exitCode
  }.drain
}
