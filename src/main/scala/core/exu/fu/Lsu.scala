package core.exu.fu

import chisel3._
import chisel3.util._
import core.CoreConfig
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

object Lsu extends CoreConfig {
  def apply(io: AXI4LiteIO)(en: Bool, rs1: UInt, rs2: UInt, op: UInt, offset: UInt) = {
    io <> DontCare // todo: remove this

    // Store type
    val wEn = en && VecInit(LsuOp.Sw, LsuOp.Sh, LsuOp.Sb).contains(op)
    io.aw.valid := wEn
    io.aw.bits.addr := (rs1 + offset).asUInt
    io.w.valid := wEn
    io.w.bits.data := rs2
    io.w.bits.strb := MuxLookup(
      key = op,
      default = 0.U,
      mapping = List(
        LsuOp.Sb -> "b0001".U,
        LsuOp.Sh -> "b0011".U,
        LsuOp.Sw -> "b1111".U,
      ),
    )

    // Load type
    io.ar.valid := en
    io.ar.bits.addr := (rs1 + offset).asUInt
    // todo: Theoretically, unaligned addresses require two memory accesses.
    val data = io.r.bits.data >> (offset << 3)
    // format: off
    MuxLookup(
      key = op,
      default = 0.U,
      mapping = List(
        LsuOp.Lb  -> Cat(Fill(XLen -  8, data( 7)), data( 7, 0)),
        LsuOp.Lh  -> Cat(Fill(XLen - 16, data(15)), data(15, 0)),
        LsuOp.Lw  -> data,
        LsuOp.Lbu -> Cat(0.U((XLen -  8).W), data( 7, 0)), // zero-extends
        LsuOp.Lhu -> Cat(0.U((XLen - 16).W), data(15, 0)), // zero-extends
      )
    )
    // format: on
  }
}
