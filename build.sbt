
import AssemblyKeys._

assemblySettings

scalaVersion := "2.10.2"

libraryDependencies += "org.apache.avro" % "avro-tools" % "1.7.5"

libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.1"

mainClass in assembly := Some("AvroToCSV")


//ouput to ./avro2csv-full.jar
jarName in assembly := "avro2csv-full.jar"

target in assembly <<= baseDirectory


resolvers ++= Seq(
  "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases"
)

