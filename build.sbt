
name := "sqsmock"

organization := "io.findify"

scalaVersion := "2.13.1"

val akkaVersion = "2.6.5"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % "10.1.12",
  "org.scala-lang.modules" %% "scala-xml" % "1.3.0",
  "org.scalatest" %% "scalatest" % "3.1.1" % "test",
  "com.typesafe" % "config" % "1.4.0",
  "com.amazonaws" % "aws-java-sdk-sqs" % "1.11.32" % "test"
)

licenses += ("MIT", url("https://opensource.org/licenses/MIT"))

resolvers += "TDR Releases" at "s3://tdr-releases-mgmt"

s3acl := None
s3sse := true
ThisBuild / publishMavenStyle := true

ThisBuild / publishTo := {
  val prefix = if (isSnapshot.value) "snapshots" else "releases"
  Some(s3resolver.value(s"My ${prefix} S3 bucket", s3(s"tdr-$prefix-mgmt")))
}


parallelExecution in Test := false