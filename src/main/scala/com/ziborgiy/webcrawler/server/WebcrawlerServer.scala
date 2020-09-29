package com.ziborgiy.webcrawler.server

import cats.Parallel
import cats.implicits._
import cats.effect.{Blocker, ConcurrentEffect, ContextShift, Timer}
import com.ziborgiy.webcrawler.operations.Titles
import fs2.Stream
import org.http4s.HttpRoutes
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger
import org.http4s.server.staticcontent.WebjarService.{Config, WebjarAsset}
import org.http4s.server.staticcontent.webjarService

import scala.concurrent.ExecutionContext.global

object WebcrawlerServer {

  def isAsset(asset: WebjarAsset): Boolean = List(".js",".css",".html").exists(asset.asset.endsWith)

  def stream[F[_] : ConcurrentEffect: Parallel](implicit T: Timer[F], C: ContextShift[F]): Stream[F, Nothing] = {
    val crawlAlg = Titles.impl[F]
    val webjars: HttpRoutes[F] = webjarService(Config(blocker = Blocker.liftExecutionContext(global), filter = isAsset))
    val httpApp = (webjars <+> WebcrawlerRoutes[F](crawlAlg).routes).orNotFound
    val finalHttpApp = Logger.httpApp(true, true)(httpApp)
    for {
      exitCode <- BlazeServerBuilder[F](global)
        .bindLocal(8081)
        .withHttpApp(finalHttpApp)
        .serve
    } yield exitCode
  }.drain
}
