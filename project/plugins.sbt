addSbtPlugin("com.timushev.sbt"   % "sbt-updates"              % "0.6.2")
addSbtPlugin("org.scala-js"       % "sbt-scalajs"              % "1.10.0")
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.1.0")
addSbtPlugin("ch.epfl.scala"      % "sbt-scalajs-bundler"      % "0.20.0")

libraryDependencies += "org.scala-js" %% "scalajs-env-jsdom-nodejs" % "1.1.0"
