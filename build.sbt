name := "ebiznes"

version := "1.0"

lazy val ebiznes = (project in file(".")).enablePlugins(PlayScala)


resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(   ehcache , ws , specs2 % Test , guice,
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.3",
  "com.typesafe.play" %% "play-slick" % "4.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "4.0.0",
  "org.xerial"        %  "sqlite-jdbc" % "3.30.1" )



//unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

      