import chisel3._
import chisel3.stage.ChiselStage
import chisel3.util.Counter
import chiseltest._
import firrtl.options.TargetDirAnnotation
import memory.AXI4Memory
import org.scalatest.flatspec.AnyFlatSpec
import utils._

class TopTester(filePath: String = "tests/asm/ld.hex") extends PanicTester {
  val top = Module(new Top)
  val imem = Module(new AXI4Memory(filePath))
  val dmem = Module(new AXI4Memory(filePath))
  imem.io <> top.io.imem
  dmem.io <> top.io.dmem

  when(top.io.exit) { stop() }
}

class TopTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior.of("Top")

  it should "success" in {

    // todo: sb
    test(new TopTester("tests/c/putchar.hex")).withAnnotations(Seq(WriteVcdAnnotation)).runUntilStop()

    (new ChiselStage).emitVerilog(
      gen = new Top,
      annotations = Seq(TargetDirAnnotation("test_run_dir/Top_should_success/")),
    )
  }
}
