package core.exu

import chisel3._
import chiseltest._
import core.idu.Idu
import org.scalatest.flatspec.AnyFlatSpec
import Chisel.testers.BasicTester
import chisel3.util.Counter
import core.RegFile

class ExuTester extends BasicTester {
  val idu = Module(new Idu)
  val exu = Module(new Exu)
  val rf = Module(new RegFile)
  idu.io.in.pc := 4.U
  rf.io <> idu.io.rg
  exu.io.in <> idu.io.out

  val insts = VecInit(
    Seq(
//      "x00a28293".U, // addi x5, x5, 10
      "x00628863".U // beq x5, x6, 16
    )
  )
  val (i, done) = Counter(true.B, insts.size)

  idu.io.in.inst := insts(i)

  when(done) { stop() }
  printf(
    cf"Case: $i, Inst: ${insts(i)}\n" +
      cf"\t${idu.io.out.data}\n" +
      cf"\t${idu.io.out.rfw}\n" +
      cf"\t${idu.io.out.fc}\n" +
      cf"\t${exu.io.out.rfw}\n" +
      cf"\t${exu.io.out.br}\n"
  )
//  printf("Inst: 0x%x, rs1: 0x%x, rs2: 0x%x\n", dut.io.in.inst, dut.io.out.data.rs1, dut.io.out.data.rs2)
}

class BruTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior.of("Exu")

  it should "success" in test(new ExuTester).runUntilStop()
}
