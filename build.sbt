val Http4sVersion = "0.21.5"
val TapirVersion = "0.16.16"
val CirceVersion = "0.13.0"
val ScalaTestVersion = "3.2.0"
val LogbackVersion = "1.2.3"
val JsoupVersion = "1.13.1"


commands ++= Seq(
  Command.command("cleanTest") { state =>
    "clean" :: "test" :: "coverageReport" :: state
  },
  Command.command("packageAssemble") { state =>
    "cleanTest" :: "assembly" :: state
  },
)

lazy val root = (project in file("."))
  .settings(
    mainClass in assembly := Some("com.ziborgiy.webcrawler.Main"),
    organization := "com.ziborgiy",
    name := "web-crawler",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "2.13.2",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s" %% "http4s-blaze-client" % Http4sVersion % "test",
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "io.circe" %% "circe-literal" % CirceVersion % "test",
      "org.scalatest" %% "scalatest" % ScalaTestVersion % "test",
      "ch.qos.logback" % "logback-classic" % LogbackVersion,
      "org.jsoup" % "jsoup" % JsoupVersion,

      "org.webjars" % "swagger-ui" % "3.20.9",

      //tapir libs
      "com.softwaremill.sttp.tapir" %% "tapir-core" % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % TapirVersion
    ),
  )
coverageEnabled.in(Test, test) := true

scalacOptions ++= Seq(
  "-encoding", "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
)
