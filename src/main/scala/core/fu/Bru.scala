package core.fu

import chisel3._
import chisel3.util.MuxLookup
import core.CoreBundle
import core.idu.BruOp
import utils._

class BranchOut extends Bundle {
  val taken = Output(Bool())
  val target = Output(UInt(32.W))
}

class BruIO extends CoreBundle {
  val rs1 = Input(UInt(XLen.W))
  val rs2 = Input(UInt(XLen.W))
  val op = Input(UInt(3.W))
  val nextPc = Input(UInt(XLen.W))
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
      BruOp.Bgeu -> (io.rs1 >= io.rs2)
    )
  )

  // JALR target address will set the least-significant bit of the result to zero.
  io.out.target := Mux(io.op === BruOp.Jalr, (io.rs1 + io.offset) >> 1.U << 1.U, io.nextPc + io.offset)
  Trace(cf"$io")
}

object Bru {
  def apply(rs1: UInt, rs2: UInt, op: UInt, nextPc: UInt, offset: UInt) = {
    val bru = Module(new Bru)
    bru.io.rs1 := rs1
    bru.io.rs2 := rs2
    bru.io.op := op
    bru.io.nextPc := nextPc
    bru.io.offset := offset
    bru.io.out
  }
}
