ThisBuild / scalaVersion     := "3.1.1"
ThisBuild / version          := "0.1.0"
ThisBuild / organization     := "pro.reiss"
ThisBuild / organizationName := "reiss.pro"

Compile / run / fork := true

Global / onChangedBuildSource := ReloadOnSourceChanges
Global / semanticdbEnabled    := true // for metals

/**
  * CATS + HTTP4S + LAMINAR
  */
lazy val root = project
  .in(file("."))
  .aggregate(`backend-http4s-cats`, `frontend-laminar-cats`, `shared-cats`.jvm, `shared-cats`.js)

/**
  * BACKEND
  */
lazy val `backend-http4s-cats` = project
  .in(file("modules/backend-http4s-cats"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-ember-server" % "1.0.0-M32",
      "org.http4s" %% "http4s-blaze-server" % "1.0.0-M32",
      "org.http4s" %% "http4s-dsl"          % "1.0.0-M32",
      "org.http4s" %% "http4s-circe"        % "1.0.0-M32",
      "io.circe"   %% "circe-generic"       % "0.15.0-M1"
    )
  )
  .dependsOn(`shared-cats`.jvm)

lazy val `backend-zio` = project
  .in(file("modules/backend-zio"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "io.d11" %% "zhttp" % "2.0.0-RC4"
    )
  )
  .dependsOn(`shared-zio`.jvm)

/**
  * FRONTEND
  */
lazy val `frontend-vanilla` = project
  .in(file("modules/frontend-vanilla"))
  .enablePlugins(ScalaJSPlugin)
  .settings(commonSettings)
  .settings(commonFrontendSettings)
  .settings(
    libraryDependencies ++= Seq(
      "org.scala-js"                  %%% "scalajs-dom" % "2.1.0",
      "com.softwaremill.sttp.client3"  %% "core"        % "3.5.1",
      "com.softwaremill.sttp.client3" %%% "circe"       % "3.5.1"
    )
  )
  .dependsOn(`shared-vanilla`.js)

lazy val `frontend-laminar-cats` = project
  .in(file("modules/frontend-laminar-cats"))
  .enablePlugins(ScalaJSPlugin)
  .settings(commonSettings)
  .settings(commonFrontendSettings)
  .settings(
    libraryDependencies ++= Seq(
      "com.raquo"                     %%% "laminar" % "0.14.2",
      "com.softwaremill.sttp.client3"  %% "core"    % "3.6.2",
      "com.softwaremill.sttp.client3" %%% "circe"   % "3.6.2"
    )
  )
  .dependsOn(`shared-cats`.js)

lazy val `frontend-tyrian-zio` = project
  .in(file("modules/frontend-tyrian-zio"))
  .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
  .settings(commonSettings)
  .settings(commonFrontendSettings)
  .settings(
    libraryDependencies ++= Seq(
      "io.indigoengine" %%% "tyrian" % "0.3.2"
    ),
    Compile / npmDependencies ++= Seq(
      "snabbdom" -> "3.0.1"
    )
  )
  .dependsOn(`shared-zio`.js)

// TODO add udash???

/**
  * SHARED
  */

lazy val `shared-vanilla` = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("modules/shared-vanilla"))
  .settings(commonSettings)

lazy val `shared-cats` = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("modules/shared-cats"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "org.typelevel" %%% "cats-core" % "2.7.0"
    )
  )

lazy val `shared-zio` = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("modules/shared-zio"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio-prelude" % "1.0.0-RC11-2"
    )
  )

val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-source:future",
    // "-Ywarn-unused",
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

val commonFrontendSettings = Seq(
  scalaJSUseMainModuleInitializer := true,
  scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
  Test / jsEnv                    := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv()
)
