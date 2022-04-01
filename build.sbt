ThisBuild / scalaVersion     := "3.1.1"
ThisBuild / version          := "0.1.0"
ThisBuild / organization     := "pro.reiss"
ThisBuild / organizationName := "reiss.pro"

Compile / run / fork := true

Global / onChangedBuildSource := ReloadOnSourceChanges
Global / semanticdbEnabled    := true // for metals

lazy val root = project
  .in(file("."))
  .aggregate(frontend, backend, shared.jvm, shared.js)

lazy val backend = project
  .in(file("modules/backend"))
  .settings(commonSettings)
  .dependsOn(shared.jvm)

lazy val frontend = project
  .in(file("modules/frontend"))
  .enablePlugins(ScalaJSPlugin)
  .settings(commonSettings)
  .settings(scalaJSUseMainModuleInitializer := true)
  .settings(
    libraryDependencies ++= Seq(
      "com.raquo" %%% "laminar" % "0.14.2"
    ),
    Test / jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv()
  )
  .dependsOn(shared.js)

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("modules/shared"))
  .settings(commonSettings)

val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-source:future",
    "-Ywarn-unused",
    "-deprecation",
    "-explain",
    "-feature",
    "-language:implicitConversions",
    "-unchecked",
    "-Xfatal-warnings",
    "-Yexplicit-nulls", // experimental (I've seen it cause issues with circe)
    "-Ykind-projector",
    "-Ysafe-init",      // experimental (I've seen it cause issues with circe)
    "-rewrite",
    "-indent"
  )
)
