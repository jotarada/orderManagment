name := "orderManagment"

version := "0.1"

scalaVersion := "2.12.8"

val akkaVersion = "2.5.23"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value
)

// JACKSON TOOLS
libraryDependencies ++= Seq(
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.8",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.8"
)