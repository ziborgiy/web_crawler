package com.ziborgiy.webcrawler.server

import cats.data.NonEmptyList
import cats.effect.{ContextShift, Sync}
import cats.implicits._
import com.ziborgiy.webcrawler.model.Codecs._
import com.ziborgiy.webcrawler.model.Urls
import com.ziborgiy.webcrawler.operations.Titles
import io.circe.generic.auto._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.Location
import org.http4s.implicits.http4sLiteralsSyntax
import sttp.tapir.{Endpoint, _}
import sttp.tapir.docs.openapi._
import sttp.tapir.json.circe._
import sttp.tapir.openapi.Info
import sttp.tapir.openapi.circe.yaml.RichOpenAPI
import sttp.tapir.server.http4s._

trait WebcrawlerRoutes[F[_]] {
  def routes: HttpRoutes[F]
}


object WebcrawlerRoutes {

  def apply[F[_]: ContextShift](T: Titles[F])(implicit F: Sync[F]): WebcrawlerRoutes[F] = new WebcrawlerRoutes[F]() {
    val dsl = new Http4sDsl[F] {}

    import dsl._

    override def routes: HttpRoutes[F] = {
      val value: NonEmptyList[HttpRoutes[F]] = NonEmptyList
        .of(
          crawlerRoutes.toRoutes(logic = getTitles),
          docsRoute,
          openApiRoute
        )
      value.reduceK
    }

    private[this] def docsRoute: HttpRoutes[F] = {
      HttpRoutes.of[F] {
        case GET -> Root / "docs" =>
          PermanentRedirect(Location(uri"/swagger-ui/3.20.9/index.html?url=/openapi.yml#/"))
      }
    }

    private[this] def openApiRoute: HttpRoutes[F] = {
      HttpRoutes.of[F] {
        case GET -> Root / "openapi.yml" => Ok(openApiYaml)
      }
    }

    def openApiYaml: String = List(crawlerRoutes).toOpenAPI(Info("Backend using Tapir",
      "1.0")).toYaml


    private[this] def crawlerRoutes: Endpoint[Urls, String, Map[String, String], Nothing] = {
      endpoint.post
        .in("getTitles")
        .in(anyJsonBody[Urls])
        .out(anyJsonBody[Map[String, String]])
        .errorOut(stringBody)
        .description("Get titles by requested urls")
    }

    private[this] def getTitles(req: Urls): F[Either[String, Map[String, String]]] = {
      T.getTitles(req.urls).map(Either.right(_))
    }
  }
}
