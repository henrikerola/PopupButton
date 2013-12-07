import org.vaadin.sbt.VaadinPlugin._

name := "PopupButton"

version in ThisBuild := "2.3-SNAPSHOT"

organization in ThisBuild := "org.vaadin.hene"

crossPaths in ThisBuild := false

autoScalaLibrary := false

lazy val root = project.in(file(".")).aggregate(addon, demo)

lazy val addon = project.settings(vaadinAddOnSettings :_*).settings(
  name := "PopupButton",
  libraryDependencies := Dependencies.addonDeps
)

lazy val demo = project.settings(vaadinWebSettings :_*).settings(
  name := "popupbutton-demo",
  artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) => "PopupButton." + artifact.extension },
  libraryDependencies := Dependencies.demoDeps,
  javaOptions in compileVaadinWidgetsets := Seq("-Xss8M", "-Xmx512M", "-XX:MaxPermSize=512M"),
  vaadinOptions in compileVaadinWidgetsets := Seq("-strict", "-draftCompile"),
  enableCompileVaadinWidgetsets in resourceGenerators := false,
  javaOptions in vaadinDevMode ++= Seq("-Xdebug", "-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005")
).dependsOn(addon)
