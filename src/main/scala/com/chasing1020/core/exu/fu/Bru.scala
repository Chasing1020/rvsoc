package com.chasing1020.core.exu.fu

import chisel3._
import chisel3.util.MuxLookup
import com.chasing1020.core.CoreBundle
import com.chasing1020.utils._

case object BruOp {
  final val Unknown: UInt = "b0000".U
  final val Jal:     UInt = "b0001".U
  final val Jalr:    UInt = "b0010".U
  final val Beq:     UInt = "b0011".U
  final val Bne:     UInt = "b0100".U
  final val Blt:     UInt = "b0101".U
  final val Bge:     UInt = "b0110".U
  final val Bltu:    UInt = "b0111".U
  final val Bgeu:    UInt = "b1000".U
}

class BranchOut extends Bundle {
  val taken = Output(Bool())
  val target = Output(UInt(32.W))
}

class BruIO extends CoreBundle {
  val rs1 = Input(UInt(XLen.W))
  val rs2 = Input(UInt(XLen.W))
  val op = Input(UInt(3.W))
  val pc = Input(UInt(XLen.W))
  val offset = Input(UInt(XLen.W))
  val out = new BranchOut
}

class Bru extends Module {
  val io = IO(new BruIO)

  io.out.taken := MuxLookup(
    key = io.op,
    default = false.B,
    mapping = List(
      BruOp.Jal -> true.B,
      BruOp.Jalr -> true.B,
      BruOp.Beq -> (io.rs1 === io.rs2),
      BruOp.Bne -> (io.rs1 =/= io.rs2),
      BruOp.Blt -> (io.rs1.asSInt < io.rs2.asSInt),
      BruOp.Bge -> (io.rs1.asSInt >= io.rs2.asSInt),
      BruOp.Bltu -> (io.rs1 < io.rs2),
      BruOp.Bgeu -> (io.rs1 >= io.rs2),
    ),
  )

  // JALR target address will set the least-significant bit of the result to zero.
  io.out.target := Mux(io.op === BruOp.Jalr, (io.rs1 + io.offset) >> 1.U << 1.U, io.pc + io.offset)

  Debug(cf"[Bru]: $io")
  Debug(cf"[Bru]: ${BruOp.Jal === io.op}")
}

object Bru {
  def apply(rs1: UInt, rs2: UInt, op: UInt, pc: UInt, offset: UInt) = {
    val bru = Module(new Bru)
    bru.io.rs1 := rs1
    bru.io.rs2 := rs2
    bru.io.op := op
    bru.io.pc := pc
    bru.io.offset := offset
    bru.io.out
  }

  def main(args: Array[String]): Unit = {
    EmitVerilog(new Bru)
  }
}
