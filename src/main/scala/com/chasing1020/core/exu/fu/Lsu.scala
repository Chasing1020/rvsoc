package com.chasing1020.core.exu.fu

import chisel3._
import chisel3.util._
import com.chasing1020.core.exu.fu.Lsu.XLen
import com.chasing1020.core.{CoreBundle, CoreConfig, CoreModule}
import com.chasing1020.memory.AXI4LiteIO
import com.chasing1020.utils._

case object LsuOp {
  final val Unknown: UInt = "b0000".U
  final val Lb:      UInt = "b0001".U
  final val Lh:      UInt = "b0010".U
  final val Lw:      UInt = "b0011".U
  final val Lbu:     UInt = "b0100".U
  final val Lhu:     UInt = "b0101".U
  final val Sb:      UInt = "b0110".U
  final val Sh:      UInt = "b0111".U
  final val Sw:      UInt = "b1000".U
}

class LsuIO extends CoreBundle {
  val rs1 = Input(UInt(XLen.W))
  val rs2 = Input(UInt(XLen.W))
  val op = Input(UInt(3.W))
  val en = Input(Bool())
  val offset = Input(UInt(XLen.W))

  val axi4 = new AXI4LiteIO

  val out = Output(UInt(XLen.W))
}

class Lsu extends CoreModule {
  val io = IO(new LsuIO)

  io.axi4 <> DontCare

  // Store type
  val wEn = io.en && VecInit(LsuOp.Sw, LsuOp.Sh, LsuOp.Sb).contains(io.op)
  io.axi4.aw.valid := wEn
  io.axi4.aw.bits.addr := (io.rs1 + io.offset).asUInt
  io.axi4.w.valid := wEn
  io.axi4.w.bits.data := io.rs2
  io.axi4.w.bits.strb := MuxLookup(
    key = io.op,
    default = 0.U,
    mapping = List(
      LsuOp.Sb -> "b0001".U,
      LsuOp.Sh -> "b0011".U,
      LsuOp.Sw -> "b1111".U,
    ),
  )

  // Load type
  io.axi4.ar.valid := io.en
  io.axi4.ar.bits.addr := (io.rs1 + io.offset).asUInt
  // todo: Theoretically, unaligned addresses require two rvsoc.memory accesses.
  val data = io.axi4.r.bits.data >> ((io.rs1 % 4.U + io.offset) << 3)
  // format: off
  io.out := MuxLookup(
    key = io.op,
    default = 0.U,
    mapping = List(
      LsuOp.Lb  -> Cat(Fill(XLen -  8, data( 7)), data( 7, 0)),
      LsuOp.Lh  -> Cat(Fill(XLen - 16, data(15)), data(15, 0)),
      LsuOp.Lw  -> data,
      LsuOp.Lbu -> Cat(0.U((XLen -  8).W), data( 7, 0)), // zero-extends
      LsuOp.Lhu -> Cat(0.U((XLen - 16).W), data(15, 0)), // zero-extends
    )
  )
}

object Lsu extends CoreConfig {
  def apply(axi4: AXI4LiteIO)(en: Bool, rs1: UInt, rs2: UInt, op: UInt, offset: UInt) = {
    val lsu = Module(new Lsu)
    lsu.io.axi4 <> axi4
    lsu.io.en := en
    lsu.io.rs1 := rs1
    lsu.io.rs2 := rs2
    lsu.io.op := op
    lsu.io.offset := offset
    lsu.io.out
  }

  def main(args: Array[String]): Unit = {
    EmitVerilog(new Lsu)
  }
}
