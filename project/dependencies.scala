import sbt._
import sbt.Keys._

object Dependencies {

  val vaadinServer = "com.vaadin" % "vaadin-server" % "7.1.2"
  val vaadinClient = "com.vaadin" % "vaadin-client" % "7.1.2"
  val vaadinClientCompiler = "com.vaadin" % "vaadin-client-compiler" % "7.1.2"
  val vaadinThemes = "com.vaadin" % "vaadin-themes" % "7.1.2"

  val servletApi = "javax.servlet" % "servlet-api" % "2.4"

  val jetty = "org.mortbay.jetty" % "jetty" % "6.1.22";

  val addonDeps = Seq(
    vaadinServer,
    vaadinClient
  )

  val demoDeps = Seq(
    servletApi % "provided",
    vaadinClientCompiler % "provided",
    vaadinThemes % "container",
    jetty % "container"
  )

}
