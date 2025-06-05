name := "scala-3-shenanigans"

version := "0.1"

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.4.12",
  "org.typelevel" %% "cats-core" % "2.12.0",
  "org.typelevel" %% "cats-effect" % "3.5.4",
  "org.typelevel" %% "log4cats-core" % "2.6.0",
  "org.typelevel" %% "log4cats-slf4j" % "2.6.0",
  "net.logstash.logback" % "logstash-logback-encoder" % "7.4",
  "dev.profunktor" %% "redis4cats-effects"  % "1.7.0",
  "dev.profunktor" %% "redis4cats-log4cats" % "1.7.0",
  "com.dimafeng" %% "testcontainers-scala" % "0.41.4",
  "com.dimafeng" %% "testcontainers-scala-scalatest" % "0.41.4",
  "org.scalatest" %% "scalatest" % "3.2.18" % Test,
  "org.scalameta" %% "munit" % "1.1.1" % Test
)

lazy val commonSettings = Seq(
  scalaVersion := "3.3.6",
  scalacOptions ++= Seq(
    "-noindent",
    "-rewrite",
    "-print-lines",
    "-java-output-version:21",
  ),
  javacOptions ++= Seq(
    " --illegal-access=warn",
    "-source", "21",
    "-target", "21"
  )
)

lazy val root = (project in file("."))
  .settings {
    commonSettings: _*
  }
