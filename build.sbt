name := """football_app"""
organization := "com.footballclub"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)


val testContainersScalaVersion = "0.40.11"  // version for test containers


scalaVersion := "2.13.10"

libraryDependencies ++= Seq(
  guice,

  // web for test containers %% name for container %% val from line 9 %% scope
  "com.dimafeng" %% "testcontainers-scala-scalatest" % testContainersScalaVersion % "test",
  "com.dimafeng" %% "testcontainers-scala-mongodb" % testContainersScalaVersion % "test",
  "org.mongodb.scala" %% "mongo-scala-driver" % "4.6.0",
  "org.webjars.npm" % "bootstrap" % "5.2.2",
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
)





// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.footballclub.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.footballclub.binders._"
