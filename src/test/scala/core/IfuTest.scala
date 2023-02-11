package core

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import scala.language.implicitConversions


class IfuTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior.of("Ifu")

  it should "success" in {
    test(new Ifu) { dut =>
      dut.clock.step(1)
      dut.io.out.inst.expect("0x1050003".U)
    }
  }
}