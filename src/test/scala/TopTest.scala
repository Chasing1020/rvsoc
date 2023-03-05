import chisel3._
import chisel3.stage.ChiselStage
import chisel3.util.Counter
import chiseltest._
import firrtl.options.TargetDirAnnotation
import memory.AXI4Memory
import org.scalatest.flatspec.AnyFlatSpec
import utils._

class TopTester extends TraceTester {
  val top = Module(new Top)
  val mem = Module(new AXI4Memory("tests/asm/hex/addi.hex"))
  mem.io <> top.io.imem
  top.io.dmem <> DontCare
  val (i, done) = Counter(true.B, 6)

  when(done) { stop() }
}

class TopTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior.of("Top")

  it should "success" in {
    test(new TopTester).withAnnotations(Seq(WriteVcdAnnotation)).runUntilStop()

    (new ChiselStage).emitVerilog(
      gen = new Top,
      annotations = Seq(TargetDirAnnotation("test_run_dir/Top_should_success/")),
    )
  }
}
