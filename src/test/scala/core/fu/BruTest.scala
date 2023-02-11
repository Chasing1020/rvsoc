package core.fu

import chisel3._
import chiseltest._
import core.idu.BruOp
import org.scalatest.flatspec.AnyFlatSpec


class BruTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior.of("Bru")

  it should "success" in {
    test(new Bru) { dut =>
      dut.io.rs1.poke(1.U)
      dut.io.rs2.poke(2.U)
      dut.io.op.poke(BruOp.Blt)
      dut.io.nextPc.poke(0.U)
      dut.io.offset.poke(4.U)

      dut.io.out.taken.expect(true.B)
      dut.io.out.target.expect(4.U)


      dut.io.rs1.poke(1.U)
      dut.io.rs2.poke(2.U)
      dut.io.op.poke(BruOp.Jalr)
      dut.io.nextPc.poke(0.U)
      dut.io.offset.poke(4.U)

      dut.io.out.taken.expect(true.B)
      dut.io.out.target.expect(4.U) // res = 5, then clear the least-significant
    }
  }
}
