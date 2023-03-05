package core

import chisel3._
import chisel3.stage.ChiselStage
import chisel3.util._
import utils._
import chiseltest._
import core.ifu.Ifu
import firrtl.options.TargetDirAnnotation
import org.scalatest.flatspec.AnyFlatSpec
import memory._

class IfuTester extends DebugTester {

  val ifu = Module(new Ifu)
  val memory = Module(new AXI4Memory("tests/asm/hex/addi.hex"))
  memory.io <> ifu.io.imem

  ifu.io.in.taken := false.B
  ifu.io.in.target := 0.U
  val (i, done) = Counter(true.B, 4)

  when(done) { stop() }

  Info(cf"Case: $i")
  Debug(cf"in  :\t${ifu.io.in}")
  Debug(cf"inst:\t${ifu.io.out.inst}%x")
  Debug(cf"pc  :\t${ifu.io.out.pc}%x")
}

class IfuTest extends AnyFlatSpec with ChiselScalatestTester {

  behavior.of("Ifu")

  it should "success" in {
    test(new IfuTester).withAnnotations(Seq(WriteVcdAnnotation)).runUntilStop()

    (new ChiselStage).emitVerilog(
      gen = new RegFile,
      annotations = Seq(TargetDirAnnotation("test_run_dir/Ifu_should_success/")),
    )
  }
}
