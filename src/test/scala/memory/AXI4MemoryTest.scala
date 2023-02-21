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

    test(new AXI4Memory(filePath)).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
//      val source = scala.io.Source.fromFile(filePath)
//      val lines =
//        try source.getLines().toSeq
//        finally source.close()

      dut.io.ar.bits.addr.poke(0.U)
      dut.clock.step(1)
      dut.io.r.bits.data.expect("x6400293".U)
    }
  }
}
