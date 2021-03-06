import java.io.{FileInputStream, FileOutputStream}

name := "agent_server"

resolvers += Resolver.mavenLocal

lazy val commonSettings = Seq(
  organization := "com.github.dapeng-soa",
  version := "2.2.1",
  scalaVersion := "2.12.2"
)

javacOptions ++= Seq("-encoding", "UTF-8")

lazy val api = (project in file("agent_server-api"))
  .settings(
    commonSettings,
    name := "agent_server-api",
    libraryDependencies ++= Seq(
      "com.github.wangzaixiang" %% "scala-sql" % "2.0.6",
      "com.google.code.gson" % "gson" % "2.3.1"
    )
  )

lazy val service = (project in file("agent_server-service"))
  .dependsOn( api )
  .settings(
    commonSettings,
    name := "agent_server_service",
    assembly/mainClass := Some("com.github.dapeng.boostrap.Boostrap"),
    libraryDependencies ++= Seq(
      "org.yaml" % "snakeyaml" % "1.17",
      "io.netty" % "netty-all" % "4.1.20.Final",
      "com.github.wangzaixiang" %% "scala-sql" % "2.0.6",
      "com.corundumstudio.socketio" % "netty-socketio" % "1.7.12" excludeAll("io.netty"),
      "io.socket" % "socket.io-client" % "0.8.1" excludeAll("io.netty"),
      "com.github.wangzaixiang" %% "scala-sql" % "2.0.6",
      "com.google.code.gson" % "gson" % "2.3.1",
      "org.springframework" % "spring-context" % "4.3.5.RELEASE",
      "org.springframework" % "spring-tx" % "4.3.5.RELEASE",
      "org.springframework" % "spring-jdbc" % "4.3.5.RELEASE",
      "org.springframework" % "spring-core" % "4.3.5.RELEASE",
      "mysql" % "mysql-connector-java" % "5.1.36",
      "org.slf4j" % "slf4j-api" % "1.7.13" ,
      "ch.qos.logback" % "logback-classic" % "1.1.3",
      "ch.qos.logback" % "logback-core" % "1.1.3",
      "com.alibaba" % "druid" % "1.0.17" excludeAll("commons-logging")
    ))


lazy val dist = taskKey[File]("make a dist scompose file")

dist := {
  println(s"aseemblyValue: ${assembly.value.getName}")

  val assemblyJar = (service/assembly).value

  println(s"assembly jar: $assemblyJar")

  val distJar = new java.io.File(target.value, "agentServer")
  val out = new FileOutputStream(distJar)

  out.write(
    """#!/usr/bin/env sh
      |exec java -jar -XX:+UseG1GC "$0" "$@"
      |""".stripMargin.getBytes)

  val inStream = new FileInputStream(assemblyJar)
  val buffer = new Array[Byte](1024)

  while( inStream.available() > 0) {
    val length = inStream.read(buffer)
    out.write(buffer, 0, length)
  }

  out.close

  distJar.setExecutable(true, false)
  println("=================================")
  println(s"build csAgentServer at ${distJar.getAbsolutePath}" )

  distJar
}
