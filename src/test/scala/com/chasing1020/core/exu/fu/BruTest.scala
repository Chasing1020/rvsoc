package com.chasing1020.core.exu.fu

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import Chisel.testers.BasicTester
import chisel3.stage.ChiselStage
import chisel3.util.Counter
import com.chasing1020.core.RegFile
import com.chasing1020.core.idu.Idu
import com.chasing1020.utils._
import firrtl.options.TargetDirAnnotation

import scala.util.Random

class BruTester extends DebugTester {
  val idu = Module(new Idu)
  val rf = Module(new RegFile)
  rf.io.r1 <> idu.io.rfr1
  rf.io.r2 <> idu.io.rfr2
  rf.io.w <> DontCare
  val insts = VecInit(
    Seq(
      "x00a302e7".U, // jalr x5, x6, 10
      "x00628863".U, // beq x5, x6, 16
    ),
  )
  val (i, done) = Counter(true.B, insts.size)

  idu.io.in.inst := insts(i)
  idu.io.in.pc := DontCare
  val rs1 = idu.io.out.data.rs1
  val rs2 = idu.io.out.data.rs2
  val offset = idu.io.out.data.offset
  val pc = idu.io.out.pc
  val op = idu.io.out.fc.op

  val bruOut = Bru(rs1 = rs1, rs2 = rs2, op = op, pc = pc + 4.U, offset = offset)
  when(done) { stop() }
  Info(cf"Case: $i, Inst: ${insts(i)}")
  Debug(cf"\t${idu.io.out.data}")
  Debug(cf"\t${idu.io.out.rfw}")
  Debug(cf"\t${idu.io.out.fc}")
  Debug(cf"\t$bruOut")
//  printf("Inst: 0x%x, rs1: 0x%x, rs2: 0x%x\n", dut.io.in.inst, dut.io.out.data.rs1, dut.io.out.data.rs2)
}

class BruTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior.of("Bru")

  it should "success" in {
    test(new BruTester).withAnnotations(Seq(WriteVcdAnnotation)).runUntilStop()

    test(new Bru).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      val input: List[Long] =
        List(0L, 1L, Random.nextInt().abs, Int.MaxValue.toLong, Int.MaxValue.toLong << 1L)
      for {
        op <- 1 to 7
        rs1 <- input
        rs2 <- input
        pc <- input
        offset <- input
      } {
        dut.io.rs1.poke(rs1.U)
        dut.io.rs2.poke(rs2.U)
        dut.io.op.poke(op.U)
        dut.io.pc.poke(pc.U)
        dut.io.offset.poke(offset.U)

        dut.clock.step(1) // for vcd
      }
    }

    (new ChiselStage).emitVerilog(
      gen = new Bru,
      annotations = Seq(TargetDirAnnotation("test_run_dir/Bru_should_success/")),
    )
  }
}
