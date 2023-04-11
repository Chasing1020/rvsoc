// import Mill dependency
import mill._
import mill.define.Sources
import mill.modules.Util
import mill.scalalib._
import mill.scalalib.TestModule._
import mill.scalalib.scalafmt.ScalafmtModule
import publish._
import scalalib._
// support BSP
import mill.bsp._

object rvsoc extends SbtModule with PublishModule with ScalafmtModule { m =>
  override def millSourcePath = os.pwd
  override def scalaVersion = "2.13.8"
  override def scalacOptions = Seq(
    "-language:reflectiveCalls",
    "-deprecation",
    "-feature",
    "-Xcheckinit",
    "-P:chiselplugin:genBundleElements",
  )
  override def ivyDeps = Agg(
    ivy"edu.berkeley.cs::chisel3:3.5.4",
  )
  override def scalacPluginIvyDeps = Agg(
    ivy"edu.berkeley.cs:::chisel3-plugin:3.5.4",
  )
  object test extends Tests with ScalaTest {
    override def ivyDeps = m.ivyDeps() ++ Agg(
      ivy"edu.berkeley.cs::chiseltest:0.5.4",
    )
  }

  override def publishVersion = "0.1.0"

  override def pomSettings = PomSettings(
    description = "Hello",
    organization = "shuosc",
    url = "https://github.com/Chasing1020/rvsoc",
    licenses = Seq(License.MIT),
    versionControl = VersionControl.github("lihaoyi", "example"),
    developers = Seq(
      Developer("Chasing1020", "Chasing1020", "https://github.com/chasing1020"),
    ),
  )
}
