import sbt._
import Keys._
import utest.jsrunner._

object Build extends sbt.Build{
  lazy val cross = new JsCrossBuild(
    name := "mg-domain",
    organization := "com.github.benhutchison",
    version := "0.1",
    scalaVersion := "2.11.2"
  )

  lazy val root = cross.root
  lazy val js = cross.js
  lazy val jvm = cross.jvm
}
