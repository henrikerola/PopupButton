import sbt._
import sbt.Keys._

object Dependencies {

  val vaadinVersion = "8.0.0.beta1"

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
    vaadinThemes
  )

}
