package com.chasing1020.core.idu

import chisel3._
import chisel3.stage.ChiselStage
import chiseltest._
import com.chasing1020.core.exu.fu.AluOp
import firrtl.options.TargetDirAnnotation
import org.scalatest.flatspec.AnyFlatSpec

class DecoderTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior.of("Decoder")

  it should "success" in {
    test(new Decoder).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.io.inst.poke("b00000000000000000000000000110111".U) // lui x0, 0
      dut.io.instType.expect(InstType.U)
      dut.io.fuName.expect(FuName.Alu)
      dut.io.opType.expect(AluOp.CopyB)
      // InstType.U, FuType.Alu, AluOp.Lui
      dut.clock.step()

      dut.io.inst.poke("b00000000000000000000000000010011".U) // addi x0, x0, x0
      dut.io.instType.expect(InstType.I)
      dut.io.fuName.expect(FuName.Alu)
      dut.io.opType.expect(AluOp.Add)
      // dut.io.out.expect(4.U)
      dut.clock.step()
    }

    (new ChiselStage).emitVerilog(
      gen = new Decoder,
      annotations = Seq(TargetDirAnnotation("test_run_dir/Decoder_should_success/")),
    )
  }
}
