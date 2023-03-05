package memory

import chisel3._
import chisel3.stage.ChiselStage
import chiseltest._
import chiseltest.simulator.WriteVcdAnnotation
import firrtl.options.TargetDirAnnotation
import org.scalatest.flatspec.AnyFlatSpec

class EndianReverserTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior.of("EndianReverser")

  it should "success" in {
    test(new EndianReverser(32)).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.io.in.poke("x93824206".U)
      dut.io.out.expect("x06428293".U)
      dut.clock.step(1)

      dut.io.in.poke("x37000000".U)
      dut.io.out.expect("x00000037".U)
    }

    test(new EndianReverser(64)).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.io.in.poke("x0001020304050607".U)
      dut.io.out.expect("x0706050403020100".U)
      dut.clock.step(1)

      dut.io.in.poke("x9382420693824206".U)
      dut.io.out.expect("x0642829306428293".U)
    }

    (new ChiselStage).emitVerilog(
      gen = new EndianReverser(64),
      annotations = Seq(TargetDirAnnotation("test_run_dir/EndianReverser_should_success/")),
    )
  }
}
