package memory

import chisel3._
import chisel3.stage.ChiselStage
import chiseltest._
import firrtl.options.TargetDirAnnotation
import org.scalatest.flatspec.AnyFlatSpec


class AXI4MemoryTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior.of("AXI4Memory")

  it should "success" in {
    test(new AXI4Memory("./tests/hex/addi.hex")).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.io.ar.bits.addr.poke(0.U)
      dut.clock.step(1)
      dut.io.r.bits.data.expect("x06428293".U)

      //      dut.io.out.inst.expect("0x1050003".U)
      dut.io.ar.bits.addr.poke(1.U)
      dut.clock.step(1)
      dut.io.r.bits.data.expect("x06430313".U)

      dut.io.ar.bits.addr.poke(2.U)
      dut.clock.step(1)
      dut.io.r.bits.data.expect("x06438393".U)
    }

    (new ChiselStage).emitVerilog(
      gen = new AXI4Memory,
      annotations = Seq(TargetDirAnnotation("test_run_dir/AXI4Memory_should_success/"))
    )
  }
}
