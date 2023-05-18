package com.chasing1020.core.ifu

import chisel3._
import com.chasing1020.core.CoreModule
import com.chasing1020.core.exu.fu.BranchOut
import com.chasing1020.core.idu.InstPcOut
import com.chasing1020.memory.AXI4LiteIO
import com.chasing1020.utils.{EmitVerilog, Trace}

class IfuIO extends Bundle {
  val in = Flipped(new BranchOut)
  val imem = new AXI4LiteIO
  val out = new InstPcOut
}

class Ifu extends CoreModule {
  val io = IO(new IfuIO)

  val pc = RegInit(ResetVector.U(XLen.W))

  pc := Mux(io.in.taken, io.in.target, pc + 4.U)

  io.imem <> DontCare
  io.imem.ar.valid := true.B
  io.imem.ar.bits.addr := pc
  io.imem.w.valid := false.B

  io.out.inst := io.imem.r.bits.data
  io.out.pc := pc

  Trace(cf"[Ifu.in]: ${io.in}")
  Trace(cf"[Ifu.out]: ${io.out}")
}

object Ifu {
  def main(args: Array[String]): Unit = {
    EmitVerilog(new Ifu)
  }
}
