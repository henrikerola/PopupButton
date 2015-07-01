import sbt._
import sbt.Keys._

object Dependencies {

  val vaadinVersion = "7.5.0"

  val vaadinServer = "com.vaadin" % "vaadin-server" % vaadinVersion
  val vaadinClient = "com.vaadin" % "vaadin-client" % vaadinVersion
  val vaadinClientCompiler = "com.vaadin" % "vaadin-client-compiler" % vaadinVersion
  val vaadinThemes = "com.vaadin" % "vaadin-themes" % vaadinVersion

  val addonDeps = Seq(
    vaadinServer,
    vaadinClient
  )

  val demoDeps = Seq(
    vaadinClientCompiler % "provided",
    vaadinThemes % "container"
  )

}
