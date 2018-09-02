name := "noMansKnowledge"

version := "0.1"

scalaVersion := "2.12.6"
scalacOptions ++= Seq(
  "-Xfatal-warnings",
  "-Ypartial-unification"
)
libraryDependencies ++= Seq(
  "net.ruippeixotog" %% "scala-scraper" % "2.1.0",
  "org.typelevel" %% "cats-core" % "1.2.0",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)

val circeVersion = "0.9.3"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)