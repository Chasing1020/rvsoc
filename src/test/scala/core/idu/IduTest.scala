package core.idu

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import Chisel.testers.BasicTester
import chisel3.util.Counter
import core.RegFile

class IduTester extends BasicTester {
  val dut = Module(new Idu)
  val rf = Module(new RegFile)
  rf.io <> dut.io.rg

  val insts = VecInit(
    Seq(
      "x00730293".U, // addi x5, x6, 7
      "x00a302e7".U, // jalr x5, x6, 10
      "x007302b3".U, // add x5, x6, x7
      "x00628863".U  // beq x5, x6, 16
    )
  )
  val (i, done) = Counter(true.B, insts.size)

  dut.io.in.inst := insts(i)
  dut.io.in.pc := DontCare

  when(done) { stop() }

  printf(cf"Case: $i, Inst: ${insts(i)}\n" +
    cf"\t${dut.io.out.data}\n" +
    cf"\t${dut.io.out.rfw}\n" +
    cf"\t${dut.io.out.fu}\n")
}

class IduTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior.of("Idu")

  it should "success" in test(new IduTester).runUntilStop()
}
