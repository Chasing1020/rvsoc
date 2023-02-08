package core

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class DecoderTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior.of("Decoder")

  it should "success" in {
    test(new Decoder { dut =>
      dut.io.inst.poke("b00000000000000000000000000110111".U) // lui x0, 0
      dut.io.out.expect(0.U)

      dut.io.inst.poke("b00000000000000000000000000010011".U) // addi x0, x0, x0
      dut.io.out.expect(4.U)
    })
  }
}
