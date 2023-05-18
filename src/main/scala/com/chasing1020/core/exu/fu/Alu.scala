package com.chasing1020.core.exu.fu

import chisel3._
import chisel3.stage.ChiselStage
import chisel3.util._
import com.chasing1020.utils._
import firrtl.options.TargetDirAnnotation

case object AluOp {
  final val Unknown: UInt = "b0000".U
  final val Add:     UInt = "b0001".U
  final val Sub:     UInt = "b0010".U
  final val And:     UInt = "b0011".U
  final val Or:      UInt = "b0100".U
  final val Xor:     UInt = "b0101".U
  final val Slt:     UInt = "b0110".U
  final val Sll:     UInt = "b0111".U
  final val Sltu:    UInt = "b1000".U
  final val Srl:     UInt = "b1001".U
  final val Sra:     UInt = "b1010".U
  final val CopyA:   UInt = "b1011".U
  final val CopyB:   UInt = "b1100".U
}

class AluIO(width: Int) extends Bundle {
  val rs1 = Input(UInt(width.W))
  val rs2 = Input(UInt(width.W))
  val op = Input(UInt(4.W))
  val out = Output(UInt(width.W))
}

class Alu(width: Int = 32) extends Module {
  val io = IO(new AluIO(width))

  val shamt = io.rs2(4, 0)
  val opList = List(
    AluOp.Add -> (io.rs1 + io.rs2),
    AluOp.Sub -> (io.rs1 - io.rs2),
    AluOp.And -> (io.rs1 & io.rs2),
    AluOp.Or -> (io.rs1 | io.rs2),
    AluOp.Xor -> (io.rs1 ^ io.rs2),
    AluOp.Slt -> (io.rs1.asSInt < io.rs2.asSInt).asUInt,
    AluOp.Sll -> (io.rs1 << shamt),
    AluOp.Sltu -> (io.rs1 < io.rs2).asUInt,
    AluOp.Srl -> (io.rs1 >> shamt),
    AluOp.Sra -> (io.rs1.asSInt >> shamt).asUInt,
    AluOp.CopyA -> io.rs1,
    AluOp.CopyB -> io.rs2,
  )
  io.out := MuxLookup(key = io.op, default = 0.U, mapping = opList)
  Trace(cf"[Alu]: $io")
}

object Alu {
  def apply(width: Int, rs1: UInt, rs2: UInt, op: UInt) = {
    val alu = Module(new Alu(width))
    alu.io.rs1 := rs1
    alu.io.rs2 := rs2
    alu.io.op := op
    alu.io.out
  }
}
