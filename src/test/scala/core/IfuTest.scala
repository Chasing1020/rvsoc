package core

import chisel3._
import chisel3.stage.ChiselStage
import chiseltest._
import core.Ifu.Ifu
import firrtl.options.TargetDirAnnotation
import org.scalatest.flatspec.AnyFlatSpec

class IfuTest extends AnyFlatSpec with ChiselScalatestTester {

  behavior.of("Ifu")

  it should "success" in {
    test(new Ifu).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
//      dut.clock.step(1)
//      dut.io.out.inst.expect("0x1050003".U)

    }

    (new ChiselStage).emitVerilog(
      gen = new RegFile,
      annotations = Seq(TargetDirAnnotation("test_run_dir/Ifu_should_success/"))
    )
  }
}
