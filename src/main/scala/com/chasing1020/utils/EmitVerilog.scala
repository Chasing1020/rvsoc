package com.chasing1020.utils

import chisel3.RawModule
import chisel3.stage.ChiselStage
import com.chasing1020.DefaultConfig
import firrtl.options.TargetDirAnnotation

object EmitVerilog {
  def apply(gen: => RawModule) = {
    println(
      (new ChiselStage)
        .emitVerilog(gen, annotations = Seq(TargetDirAnnotation(DefaultConfig.targetDir))),
    )
  }
}
