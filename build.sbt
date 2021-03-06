name := "keylity"

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "2.4.15" % "test"
)

scalacOptions in Test ++= Seq("-Yrangepos")

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)