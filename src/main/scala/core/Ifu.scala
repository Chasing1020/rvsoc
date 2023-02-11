package core

import chisel3._
import memory._

class BranchIO extends Bundle {
  val taken = Input(Bool())
  val target = Input(UInt(32.W))
}

class Ifu extends CoreModule {
  val io = IO(new Bundle() {
    val mem = new MemIO // todo: convert to AXI4
    val br = new BranchIO

    val out = Flipped(new ControlIO)
  })

  val pc = RegInit(ResetVector.U(XLen.W))

  pc := Mux(io.br.taken, io.br.target, pc + 4.U)
  io.mem := DontCare

//  io.out.inst := io.mem.dataOut
  io.out.inst := "b00000000000100000000010110010011".U // addi x11, x0, 1
  io.out.pc := pc
}
