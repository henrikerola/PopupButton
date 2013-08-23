import org.vaadin.sbt.VaadinPlugin._

import org.vaadin.sbt.VaadinKeys._

name := "PopupButton"

version := "2.3-SNAPSHOT"

organization := "org.vaadin.hene"

lazy val root = project.in(file(".")).aggregate(addon, demo)

lazy val addon = project.settings(vaadinAddOnSettings :_*)settings(
  name := "PopupButton",
  libraryDependencies := Dependencies.addonDeps
  //widgetsets in compileWidgetsets := Seq("org.vaadin.hene.popupbutton.widgetset.PopupbuttonWidgetset")
)

lazy val demo = project.settings(vaadinSettings :_*).settings(
  name := "popupbutton-demo",
  libraryDependencies := Dependencies.demoDeps,
  //widgetsets in compileWidgetsets := Seq("org.vaadin.hene.popupbutton.widgetset.PopupbuttonDemoWidgetset"),
  javaOptions in compileWidgetsets := Seq("-Xss8M", "-Xmx512M", "-XX:MaxPermSize=512M"),
  options in compileWidgetsets := Seq("-strict", "-draftCompile")
).dependsOn(addon)
