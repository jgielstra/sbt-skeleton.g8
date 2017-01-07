name := "$name$"

organization := "$organization$"

version := "$version$"

scalaVersion := "2.12.0"

crossScalaVersions := Seq("2.11.8", "2.12.0")

lazy val http4sVersion = "0.15.0a"

resolvers ++= Seq(
    "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
)

mainClass in Compile := Some("$organization$.$name;format=\"lower,word\"$.Main")

enablePlugins(JavaAppPackaging)

libraryDependencies ++= Seq(
    "net.logstash.logback" % "logstash-logback-encoder" % "4.4",
    "io.verizon.journal" %% "core" % "3.0.18",
    "org.http4s" %% "http4s-dsl" % http4sVersion,
    "org.http4s" %% "http4s-blaze-server" % http4sVersion,
    "org.http4s" %% "http4s-argonaut" % http4sVersion,
    "org.http4s" %% "http4s-blaze-client" % http4sVersion,
    "org.scalatest" %% "scalatest" % "3.0.0" % "test",
    "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
)

scalacOptions ++= Seq(
    "-target:jvm-1.8",
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-language:experimental.macros",
    "-unchecked",
    //"-Ywarn-unused-import",
    "-Ywarn-nullary-unit",
    "-Xfatal-warnings",
    "-Xlint",
    //"-Yinline-warnings",
    "-Ywarn-dead-code",
    "-Xfuture")

initialCommands := "import $organization$.$name;format=\"lower,word\"$._"