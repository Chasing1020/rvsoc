package core.idu

import chisel3._
import chisel3.stage.ChiselStage
import chiseltest._
import firrtl.options.TargetDirAnnotation
import org.scalatest.flatspec.AnyFlatSpec

class ImmGenTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior.of("ImmGen")

  it should "success" in {
    test(new ImmGen()).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.io.inst.poke("x00100593".U) // addi x11, x0, 1
      dut.io.instType.poke(InstType.I)
      dut.io.imm.expect(1.U)

      dut.io.inst.poke("x00001037".U) // lui x0, 4096
      dut.io.instType.poke(InstType.U)
      dut.io.imm.expect(4096.U)

      dut.io.inst.poke("x0640006f".U) // jal x0, 100
      dut.io.instType.poke(InstType.J)
      dut.io.imm.expect(100.U)

      dut.io.inst.poke("x00000523".U) // sb x0, 10(x0)
      dut.io.instType.poke(InstType.S)
      dut.io.imm.expect(10.U)

      dut.io.inst.poke("x00100263".U) // beq x0, x1, 4
      dut.io.instType.poke(InstType.B)
      dut.io.imm.expect(4.U)
    }

    (new ChiselStage)
      .emitVerilog(new ImmGen, annotations = Seq(TargetDirAnnotation("test_run_dir/ImmGen_should_success/")))
  }
}
