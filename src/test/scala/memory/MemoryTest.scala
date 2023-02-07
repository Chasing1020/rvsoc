package memory

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import scala.language.implicitConversions

class MemoryTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior.of("Memory")

  it should "success" in {
    test(new Memory) { dut =>
      dut.io.writeEnable.poke(true.B)
      dut.io.writeAddr.poke(1.U)
      dut.io.dataIn.poke(6.U)
      dut.clock.step(1)

      dut.io.writeEnable.poke(true.B)
      dut.io.writeAddr.poke(2.U)
      dut.io.dataIn.poke(7.U)
      dut.clock.step(1)

      dut.io.readEnable.poke(true.B)
      dut.io.readAddr.poke(1.U)
      dut.clock.step(1)
      dut.io.dataOut.expect(6.U)

      dut.io.readEnable.poke(true.B)
      dut.io.readAddr.poke(2.U)
      dut.clock.step(1)
      dut.io.dataOut.expect(7.U)
    }
  }
}
