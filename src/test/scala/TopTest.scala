import chisel3._
import chisel3.stage.ChiselStage
import chisel3.util.Counter
import chiseltest._
import firrtl.options.TargetDirAnnotation
import memory.AXI4Memory
import org.scalatest.flatspec.AnyFlatSpec
import utils._

class TopTester(filePath: String = "tests/asm/add.hex") extends DebugTester {
  val top = Module(new Top)
  val mem = Module(new AXI4Memory(filePath))
  mem.io <> top.io.imem
  top.io.dmem <> DontCare
//  val (i, done) = Counter(true.B, 6)

  when(top.io.exit) { stop() }
}

class TopTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior.of("Top")

  it should "success" in {

    // todo: lb, sb
    test(new TopTester("tests/asm/lui.hex")).withAnnotations(Seq(WriteVcdAnnotation)).runUntilStop()

    (new ChiselStage).emitVerilog(
      gen = new Top,
      annotations = Seq(TargetDirAnnotation("test_run_dir/Top_should_success/")),
    )
  }
}
