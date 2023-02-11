package core.idu

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import scala.language.implicitConversions


class ImmGenTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior.of("ImmGen")

  it should "success" in {
    test(new ImmGen()) { dut =>
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
  }
}
