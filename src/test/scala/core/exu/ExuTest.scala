package core.exu

import chisel3._
import chisel3.stage.ChiselStage
import chiseltest._
import core.idu.Idu
import org.scalatest.flatspec.AnyFlatSpec
import chisel3.util.Counter
import core.RegFile
import firrtl.options.TargetDirAnnotation
import utils._

class ExuTester extends DebugTester {
  val idu = Module(new Idu)
  val exu = Module(new Exu)
  val rf = Module(new RegFile)
  idu.io.in.pc := 4.U
  rf.io.r1 <> idu.io.rfr1
  rf.io.r2 <> idu.io.rfr2
  exu.io.in <> idu.io.out

  val insts = VecInit(
    Seq(
      "x00a28313".U, // addi x6, x5, 10
      "x00a30393".U // addi x7, x6, 10
//      "x00002037".U, // lui x0, 8192
//      "x00002297".U, // auipc x5, 8192; rd = PC + (imm << 12)
//      "x00a28293".U, // addi x5, x5, 10
//      "x06431293".U, // slli x5, x6, 100
//      "x00628863".U // beq x5, x6, 16
    )
  )
  val (i, done) = Counter(true.B, insts.size)

  idu.io.in.inst := insts(i)

  when(done) { stop() }

  Info(cf"Case: $i, Inst: ${insts(i)}")
  Debug(cf"${idu.io.out.data}")
  Debug(cf"${idu.io.out.fc}")
  Debug(cf"${exu.io.out.rfw}")
  Debug(cf"${exu.io.out.br}")
//  printf("Inst: 0x%x, rs1: 0x%x, rs2: 0x%x\n", dut.io.in.inst, dut.io.out.data.rs1, dut.io.out.data.rs2)
}

class BruTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior.of("Exu")

  it should "success" in {
    test(new ExuTester).withAnnotations(Seq(WriteVcdAnnotation)).runUntilStop()

    (new ChiselStage).emitVerilog(
      gen = new Exu,
      annotations = Seq(TargetDirAnnotation("test_run_dir/Exu_should_success/"))
    )
  }
}
