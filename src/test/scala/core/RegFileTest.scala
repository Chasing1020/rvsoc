package core

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import scala.language.implicitConversions

class RegFileTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior.of("RegFile")

  it should "success" in {
    test(new RegFile) { dut =>
      dut.io.waddr.poke(0.U)
      dut.io.wdata.poke(1.U)
      dut.io.wen.poke(true.B)
      dut.clock.step(1)

      dut.io.waddr.poke(5.U)
      dut.io.wdata.poke(6.U)
      dut.io.wen.poke(true.B)
      dut.clock.step(1)

      dut.io.waddr.poke(6.U)
      dut.io.wdata.poke(7.U)
      dut.io.wen.poke(true.B)
      dut.clock.step(1)

      dut.io.raddr1.poke(0.U)
      dut.io.rdata1.expect(0.U)
      dut.io.raddr2.poke(5.U)
      dut.io.rdata2.expect(6.U)
      dut.io.raddr1.poke(6.U)
      dut.io.rdata1.expect(7.U)
    }
  }
}
