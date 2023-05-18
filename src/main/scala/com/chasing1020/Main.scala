package com.chasing1020

import chisel3.stage._
import firrtl.options.TargetDirAnnotation
import DefaultConfig.{logLevel, targetDir}
import com.chasing1020.utils._

object DefaultConfig {
  def targetDir = "./build"
  def logLevel = Level.Never
}

object Main extends App {
  Logger.level = logLevel

  new ChiselStage().execute(
    args,
    Seq(
      ThrowOnFirstErrorAnnotation,
      ChiselGeneratorAnnotation(() => new Top),
      TargetDirAnnotation(if (args.isEmpty) targetDir else args.head),
    ),
  )
}
