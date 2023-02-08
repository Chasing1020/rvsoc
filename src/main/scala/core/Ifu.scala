package core

import chisel3._
import chisel3.util._
import memory._

class BranchIO extends Bundle {
  val taken = Input(Bool())
  val target = Input(UInt(32.W))
}

class Ifu extends CfgModule {
  val io = IO(new Bundle() {
    val mem = new MemIO
    val out = Flipped(new ControlIO)
    val br = new BranchIO
  })

  val pc = RegInit(ResetVector.U(XLen.W))

  pc := Mux(io.br.taken, io.br.target, pc + 4.U)

}
