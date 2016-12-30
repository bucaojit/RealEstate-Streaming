name := "RealEstate Spark Reader"

version := "1.0"

scalaVersion := "2.10.5"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.6.0"

libraryDependencies += "org.apache.spark" %% "spark-streaming" % "1.6.0"

libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka" % "1.6.0"

libraryDependencies += "org.xerial.snappy" % "snappy-java" % "1.1.1.6"
