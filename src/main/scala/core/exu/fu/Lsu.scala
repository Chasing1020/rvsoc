package core.exu.fu

import chisel3._
import memory.AXI4LiteIO
import utils._

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

object Lsu {
  def apply(io: AXI4LiteIO)(en: Bool, rs1: UInt, rs2: UInt, op: UInt, offset: UInt) = {
    val wEn = en && VecInit(LsuOp.Sw, LsuOp.Sh, LsuOp.Sb).contains(op)

    io <> DontCare
    io.ar.valid := en
    io.ar.bits.addr := (rs1 + offset).asUInt

    io.aw.valid := wEn
    io.aw.bits.addr := (rs1 + offset).asUInt
    // todo: add rw
    io.w.valid := wEn
    io.w.bits.data := rs2

    Error(cf"[Lsu.io]: ${io}")
    io.r.bits.data
  }
}
