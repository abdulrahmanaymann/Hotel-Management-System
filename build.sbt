ThisBuild / version := "0.1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % "2.8.0",
  "ch.qos.logback" % "logback-classic" % "1.4.7",
  "com.typesafe.akka" %% "akka-slf4j" % "2.8.0"
)



ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "Hotel Management System"
  )
