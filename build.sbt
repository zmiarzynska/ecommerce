name := "ebiznes"

version := "1.0"

lazy val ebiznes = (project in file(".")).enablePlugins(PlayScala)


resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
resolvers += "Atlassian's Maven Public Repository" at "https://packages.atlassian.com/maven-public/"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(   ehcache , ws , specs2 % Test , guice,
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.3",
  "com.typesafe.play" %% "play-slick" % "4.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "4.0.0",
  "org.xerial"        %  "sqlite-jdbc" % "3.30.1" ,
  "com.iheart" %% "ficus" % "1.4.7",
  "com.mohiva" %% "play-silhouette" % "7.0.0",
  "com.mohiva" %% "play-silhouette-password-bcrypt" % "7.0.0",
  "com.mohiva" %% "play-silhouette-persistence" % "7.0.0",
  "com.mohiva" %% "play-silhouette-crypto-jca" % "7.0.0",
  "com.mohiva" %% "play-silhouette-totp" % "7.0.0",
  "net.codingwell" %% "scala-guice" % "5.0.1")

libraryDependencies += ehcache

//unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

      