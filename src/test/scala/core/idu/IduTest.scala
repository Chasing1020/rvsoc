package core.idu

import chisel3._
import chiseltest._
import chisel3.util._
import org.scalatest.flatspec.AnyFlatSpec
import Chisel.testers.BasicTester
import chisel3.stage.ChiselStage
import core.RegFile
import firrtl.options.TargetDirAnnotation
import utils._

class IduTester extends DebugTester {
  Logger.level = Level.Trace

  val idu = Module(new Idu)
  val rf = Module(new RegFile)
  rf.io.r1 <> idu.io.rfr1
  rf.io.r2 <> idu.io.rfr2
  rf.io.w <> DontCare

  val insts = VecInit(
    Seq(
      "x00a28293".U, // addi x5, x5, 10
      "x00a302e7".U, // jalr x5, x6, 10
      "x007302b3".U, // add x5, x6, x7
      "x00628863".U // beq x5, x6, 16
    )
  )
  val (i, done) = Counter(true.B, insts.size)

  idu.io.in.inst := insts(i)
  idu.io.in.pc := DontCare

  when(done) { stop() }

  Info(cf"Case: $i, Inst: ${insts(i)}")
  Debug(cf"\t${idu.io.out.data}")
  Debug(cf"\t${idu.io.out.rfw}")
  Debug(cf"\t${idu.io.out.fc}")
}

class LookUpTest extends BasicTester {
  val rs1 :: rs2 :: Nil = MuxList(
    addr = 3.U,
    default = List(0.U, 0.U),
    mapping = Array(
      1.U -> List(1.U, 4.U), // rd = rs1 op imm; rd = PC+4, PC = rs1 + imm
      2.U -> List(1.U, 2.U), // M[rs1+imm][0:x] = rs2[0:x]
      3.U -> List(1.U, 2.U), // if(rs1 op rs2) PC += imm
      4.U -> List(3.U, 4.U), // rd = PC + (imm op)
      5.U -> List(3.U, 4.U), // e.g. rd = PC+4; PC += imm
      6.U -> List(1.U, 2.U) // rd = rs1 - rs2
    )
  )
  printf(cf"rs1: $rs1, rs2: $rs2\n") // rs1:  1, rs2:  4
  stop()
}

class IduTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior.of("Idu")

  it should "success" in {
    test(new IduTester).withAnnotations(Seq(WriteVcdAnnotation)).runUntilStop()

    (new ChiselStage).emitVerilog(
      gen = new Idu,
      annotations = Seq(TargetDirAnnotation("test_run_dir/Idu_should_success/"))
    )
  }

  "LookUpTest" should "pass" in test(new LookUpTest).runUntilStop()
}
