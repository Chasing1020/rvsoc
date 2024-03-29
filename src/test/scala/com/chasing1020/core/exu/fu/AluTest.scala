package com.chasing1020.core.exu.fu

import chisel3._
import chisel3.stage.ChiselStage
import chiseltest._
import chiseltest.simulator.WriteVcdAnnotation
import com.chasing1020.utils._
import firrtl.options.TargetDirAnnotation
import org.scalatest.flatspec.AnyFlatSpec

import scala.language.implicitConversions
import scala.util.Random

object ScalaAlu {
  def asUnsigned(a: Long): Long = (a + 0x100000000L) % 0x100000000L
  def asSigned(a:   Long): Long = a.toInt

  implicit def toLong(b: Boolean): Long = if (b) 1L else 0L

  def apply(op: BigInt, a: Long, b: Long) = {
    val shamt = b & 0x1f
    val table: Map[BigInt, Long] = Map(
      AluOp.Add.litValue -> (a + b),
      AluOp.Sub.litValue -> (a - b),
      AluOp.And.litValue -> (a & b),
      AluOp.Or.litValue -> (a | b),
      AluOp.Xor.litValue -> (a ^ b),
      AluOp.Slt.litValue -> (asSigned(a) < asSigned(b)),
      AluOp.Sll.litValue -> (asSigned(a) << shamt),
      AluOp.Sltu.litValue -> (asUnsigned(a) < asUnsigned(b)),
      AluOp.Srl.litValue -> (asUnsigned(a) >> shamt),
      AluOp.Sra.litValue -> (asSigned(a) >> shamt),
      AluOp.CopyA.litValue -> a,
      AluOp.CopyB.litValue -> b,
    )
    asUnsigned(table.getOrElse(op, 0L))
  }
}

class AluTest extends AnyFlatSpec with ChiselScalatestTester {
  val targetDir = "test_run_dir/Alu32_should_success/"
  Logger.level = Level.Debug

  behavior.of("Alu32")

  it should "success" in {
    test(new Alu(32)).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      val input: List[Long] =
        List(0L, 1L, Random.nextInt().abs, Int.MaxValue.toLong, Int.MaxValue.toLong << 1L)
      for {
        op <- 0 to 9
        a <- input
        b <- input
      } {
        dut.io.rs1.poke(a.U)
        dut.io.rs2.poke(b.U)
        dut.io.op.poke(op.U)
//        println(s"a: $a, b: $b, op: $op, expected: ${ScalaAlu(op, a, b)}")
        dut.io.out.expect(ScalaAlu(BigInt(op), a, b).U, s"a: $a, b: $b, op: $op")

        dut.clock.step(1) // for vcd
      }
    }

    (new ChiselStage).emitVerilog(
      gen = new Alu(32),
      annotations = Seq(TargetDirAnnotation(targetDir)),
    )
  }
}
