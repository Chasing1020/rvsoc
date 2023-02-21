package memory

import chisel3._
import chisel3.stage.ChiselStage
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import utils._

class AXI4MemoryTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior.of("AXI4Memory")

  it should "success" in {
    val filePath = "./tests/hex/addi.hex"

//    test(new AXI4Memory(filePath)).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
////      val source = scala.io.Source.fromFile(filePath)
////      val lines =
////        try source.getLines().toSeq
////        finally source.close()
//
//      dut.io.ar.bits.addr.poke(0.U)
//      dut.clock.step(1)
//      dut.io.r.bits.data.expect("x6400293".U)
//    }

    test(new AXI4Memory()).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.io.w.bits.data.poke("x01020304".U)
      dut.io.w.bits.strb.poke("b0101".U)
      dut.io.aw.bits.addr.poke(0.U)
      dut.io.w.valid.poke(true.B)
      dut.io.aw.valid.poke(true.B)
      dut.clock.step(1)

      dut.io.ar.bits.addr.poke(0.U)
      dut.io.r.bits.data.expect("x00020004".U)
      dut.clock.step(1)

      dut.io.w.bits.data.poke("x01020304".U)
      dut.io.w.bits.strb.poke("b0111".U)
      dut.io.aw.bits.addr.poke(0.U)
      dut.io.w.valid.poke(true.B)
      dut.io.aw.valid.poke(true.B)
      dut.clock.step(1)

      dut.io.ar.bits.addr.poke(0.U)
      dut.io.r.bits.data.expect("x00020304".U)
      dut.clock.step(1)

      dut.io.w.bits.data.poke("x05060708".U)
      dut.io.w.bits.strb.poke("b1111".U)
      dut.io.aw.bits.addr.poke(4.U)
      dut.io.w.valid.poke(true.B)
      dut.io.aw.valid.poke(true.B)
      dut.clock.step(1)

      dut.io.ar.bits.addr.poke(4.U)
      dut.io.r.bits.data.expect("x05060708".U)
      dut.clock.step(1)
    }
  }
}
