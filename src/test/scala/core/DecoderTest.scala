package core

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class DecoderTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior.of("Decoder")

  it should "success" in {
    test(new Decoder { dut =>
      dut.io.inst.poke("b00000000000000000000000000110111".U) // lui x0, 0
      dut.io.instType.expect(InstType.U)
      dut.io.fuType.expect(FuType.Alu)
      dut.io.opType.expect(AluOp.Lui)
      // InstType.U, FuType.Alu, AluOp.Lui
      dut.io.inst.poke("b00000000000000000000000000010011".U) // addi x0, x0, x0
      dut.io.instType.expect(InstType.I)
      dut.io.fuType.expect(FuType.Alu)
      dut.io.opType.expect(AluOp.Add)
      // dut.io.out.expect(4.U)
    })
  }
}
