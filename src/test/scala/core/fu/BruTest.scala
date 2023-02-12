package core.fu

import chisel3._
import chiseltest._
import core.idu.{BruOp, Idu}
import org.scalatest.flatspec.AnyFlatSpec
import Chisel.testers.BasicTester
import chisel3.util.Counter
import core.RegFile

class IduTester extends BasicTester {
  val dut = Module(new Bru)
  val rf = Module(new RegFile)
  val idu = Module(new Idu)
  rf.io <> idu.io.rg

  val insts = VecInit(
    Seq(
      "xfe100ee3".U, // beq x0, x1, -4
      "x00a08067".U // jalr x0, x1, 10
    )
  )

  val (cnt, done) = Counter(true.B, insts.size)

  idu.io.in.pc := 0.U
  idu.io.in.inst := insts(cnt) // addi x1, x0, 100

  dut.io.rs1 := idu.io.out.data.rs1
  dut.io.rs2 := idu.io.out.data.rs2
  dut.io.op := idu.io.out.fu.op
  dut.io.nextPc := idu.io.out.pc + 4.U
  dut.io.offset := idu.io.out.data.rs2

  when(done) { stop() }
//  printf("Inst: 0x%x, rs1: 0x%x, rs2: 0x%x\n", dut.io.in.inst, dut.io.out.data.rs1, dut.io.out.data.rs2)
}

class BruTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior.of("Bru")

  it should "success" in {
    test(new Bru) { dut =>
      dut.io.rs1.poke(1.U)
      dut.io.rs2.poke(2.U)
      dut.io.op.poke(BruOp.Blt)
      dut.io.nextPc.poke(0.U)
      dut.io.offset.poke(4.U)

      dut.io.out.taken.expect(true.B)
      dut.io.out.target.expect(4.U)

      dut.io.rs1.poke(1.U)
      dut.io.rs2.poke(2.U)
      dut.io.op.poke(BruOp.Jalr)
      dut.io.nextPc.poke(0.U)
      dut.io.offset.poke(4.U)

      dut.io.out.taken.expect(true.B)
      dut.io.out.target.expect(4.U) // res = 5, then clear the least-significant

      dut.io.rs1.poke(1.U)
      dut.io.rs2.poke(2.U)
      dut.io.op.poke(BruOp.Jal)
      dut.io.nextPc.poke(0.U)
      dut.io.offset.poke(4.U)
    }
  }
}
