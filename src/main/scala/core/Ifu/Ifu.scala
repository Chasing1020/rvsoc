package core.Ifu

import chisel3._
import core.idu.InstPcOut
import core.CoreModule
import core.exu.fu.BranchOut
import memory._
import utils._

class IfuIO extends Bundle {
  val in = Flipped(new BranchOut)
  val mem = new MemIO // todo: convert to AXI4
  val out = new InstPcOut
}

class Ifu extends CoreModule {
  val io = IO(new IfuIO)

  val pc = RegInit(ResetVector.U(XLen.W))

  pc := Mux(io.in.taken, io.in.target, pc + 4.U)
  io.mem := DontCare

  io.out.inst := io.mem.dataOut
  io.out.pc := pc

  Trace(cf"[Ifu.in]: ${io.in}")
  Trace(cf"[Ifu.out]: ${io.out}")
}
