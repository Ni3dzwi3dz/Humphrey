name := "Humphrey"
version :="0.1.0"

lazy val akkaHttpVersion = "10.4.0"
lazy val akkaVersion    = "2.7.0"


fork := true

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "com.example",
      scalaVersion    := "2.13.4"
    )),
    name := "Humphrey",
    libraryDependencies ++= Seq(
      // Akka part
      "com.typesafe.akka" %% "akka-http"                % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json"     % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion,
      "com.typesafe.akka" %% "akka-stream"              % akkaVersion,
      "ch.qos.logback"    % "logback-classic"           % "1.4.4",

      "com.typesafe.akka" %% "akka-http-testkit"        % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"                % "3.2.14"         % Test,

      // Database handling - Slick
      "com.typesafe.slick" %% "slick"           % "3.4.1",
      "com.h2database"      % "h2"              % "2.1.214",
      "org.slf4j"           % "slf4j-nop"       % "2.0.3",
      "org.xerial"          % "sqlite-jdbc"     % "3.39.3.0",
      "com.typesafe.slick" %% "slick-hikaricp"  % "3.4.1"
    )
  )
