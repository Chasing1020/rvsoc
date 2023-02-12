package core.idu

import chisel3._
import chiseltest._
import chisel3.util._
import org.scalatest.flatspec.AnyFlatSpec
import Chisel.testers.BasicTester
import core.RegFile
import utils._

class IduTester extends BasicTester {
  Logger.level = Level.Trace

  val dut = Module(new Idu)
  val rf = Module(new RegFile)
  rf.io <> dut.io.rg

  val insts = VecInit(
    Seq(
      "x00a28293".U, // addi x5, x5, 10
      "x00a302e7".U, // jalr x5, x6, 10
      "x007302b3".U, // add x5, x6, x7
      "x00628863".U // beq x5, x6, 16
    )
  )
  val (i, done) = Counter(true.B, insts.size)

  dut.io.in.inst := insts(i)
  dut.io.in.pc := DontCare

  when(done) { stop() }

  Info(cf"Case: $i, Inst: ${insts(i)}")
  Debug(cf"\t${dut.io.out.data}")
  Debug(cf"\t${dut.io.out.rfw}")
  Debug(cf"\t${dut.io.out.fc}")
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

  it should "success" in test(new IduTester).runUntilStop()

  it should "pass" in test(new LookUpTest).runUntilStop()
}
