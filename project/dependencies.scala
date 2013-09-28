import sbt._
import sbt.Keys._

object Dependencies {

  val vaadinVersion = "7.1.6"

  val vaadinServer = "com.vaadin" % "vaadin-server" % vaadinVersion
  val vaadinClient = "com.vaadin" % "vaadin-client" % vaadinVersion
  val vaadinClientCompiler = "com.vaadin" % "vaadin-client-compiler" % vaadinVersion
  val vaadinThemes = "com.vaadin" % "vaadin-themes" % vaadinVersion

  val servletApi = "javax.servlet" % "servlet-api" % "2.5"

  val jetty = "org.eclipse.jetty" % "jetty-webapp" % "8.1.11.v20130520"

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
