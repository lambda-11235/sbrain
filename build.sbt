
lazy val root = (project in file(".")).settings(
 scalaVersion := "2.11.6",
 name := "brain"
)

libraryDependencies += "org.ow2.asm" % "asm" % "5.0.4"
libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4"

libraryDependencies += "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.3"
libraryDependencies += "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.3"

// libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.13.0"
