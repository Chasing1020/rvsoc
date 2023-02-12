package core.fu

import chisel3._
import chiseltest._
import core.idu.{BruOp, Idu}
import org.scalatest.flatspec.AnyFlatSpec
import Chisel.testers.BasicTester
import chisel3.util.Counter
import core.RegFile

class BruTester extends BasicTester {
  val idu = Module(new Idu)
  val rf = Module(new RegFile)
  rf.io <> idu.io.rg

  val insts = VecInit(
    Seq(
      "x00a302e7".U, // jalr x5, x6, 10
      "x00628863".U // beq x5, x6, 16
    )
  )
  val (i, done) = Counter(true.B, insts.size)

  idu.io.in.inst := insts(i)
  idu.io.in.pc := DontCare
  val rs1 = idu.io.out.data.rs1
  val rs2 = idu.io.out.data.rs2
  val offset = idu.io.out.data.offset
  val pc = idu.io.out.pc
  val op = idu.io.out.fc.op

  val bruOut = Bru(rs1 = rs1, rs2 = rs2, op = op, nextPc = pc + 4.U, offset = offset)
  when(done) { stop() }
  printf(
    cf"Case: $i, Inst: ${insts(i)}\n" +
      cf"\t${idu.io.out.data}\n" +
      cf"\t${idu.io.out.rfw}\n" +
      cf"\t${idu.io.out.fc}\n" +
      cf"\t$bruOut\n"
  )
//  printf("Inst: 0x%x, rs1: 0x%x, rs2: 0x%x\n", dut.io.in.inst, dut.io.out.data.rs1, dut.io.out.data.rs2)
}

class BruTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior.of("Bru")

  it should "success" in test(new BruTester).runUntilStop()
}
