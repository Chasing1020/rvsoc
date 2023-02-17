package core

import chisel3._
import chisel3.stage.ChiselStage
import chiseltest._
import firrtl.options.TargetDirAnnotation
import org.scalatest.flatspec.AnyFlatSpec

class RegFileTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior.of("RegFile")

  it should "success" in {
    test(new RegFile) { dut =>
      dut.io.w.addr.poke(0.U)
      dut.io.w.data.poke(1.U)
      dut.io.w.en.poke(true.B)
      dut.clock.step(1)

      dut.io.w.addr.poke(5.U)
      dut.io.w.data.poke(6.U)
      dut.io.w.en.poke(true.B)
      dut.clock.step(1)

      dut.io.w.addr.poke(6.U)
      dut.io.w.data.poke(7.U)
      dut.io.w.en.poke(true.B)
      dut.clock.step(1)

      dut.io.r1.addr.poke(0.U)
      dut.io.r1.data.expect(0.U)
      dut.io.r2.addr.poke(5.U)
      dut.io.r2.data.expect(6.U)
      dut.io.r1.addr.poke(6.U)
      dut.io.r1.data.expect(7.U)
    }

    (new ChiselStage).emitVerilog(
      gen = new RegFile,
      annotations = Seq(TargetDirAnnotation("test_run_dir/AXI4Memory_should_success/"))
    )
  }
}
