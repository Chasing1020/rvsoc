package core

import chisel3._
import core.fu.BranchOut
import core.idu.InstPcOut
import memory._
import utils._


class Ifu extends CoreModule {
  val io = IO(new Bundle() {
    val mem = new MemIO // todo: convert to AXI4
    val br = Flipped(new BranchOut)

    val out = new InstPcOut
  })

  val pc = RegInit(ResetVector.U(XLen.W))

  pc := Mux(io.br.taken, io.br.target, pc + 4.U)
  io.mem := DontCare

  io.out.inst := io.mem.dataOut
  io.out.pc := pc

  Trace(cf"$io")
}
