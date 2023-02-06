package core

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class AdderTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior.of("Adder")

  it should "success" in {
    test(new Adder(4)) { dut =>
      for {
        a <- -4 until 3
        b <- -4 until 3
      } {
        dut.io.a.poke(a)
        dut.io.b.poke(b)
        dut.io.out.expect((a + b).S, s"a: $a, b: $b")
      }
    }
  }
}
