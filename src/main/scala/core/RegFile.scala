package core

import chisel3._
import chisel3.util._

class RegFileIO extends CfgBundle {
  val raddr1 = Input(UInt(log2Ceil(RegNum).W))
  val rdata1 = Output(UInt(XLen.W))

  val raddr2 = Input(UInt(log2Ceil(RegNum).W))
  val rdata2 = Output(UInt(XLen.W))

  val wen = Input(Bool())
  val waddr = Input(UInt(log2Ceil(RegNum).W))
  val wdata = Input(UInt(XLen.W))
}

class RegFile extends CfgModule {
  val io = IO(new RegFileIO)

  val regs = Mem(RegNum, UInt(XLen.W))
  io.rdata1 := regs(io.raddr1)
  io.rdata2 := regs(io.raddr2)
  when(io.wen & io.waddr.orR) {
    regs(io.waddr) := io.wdata
  }
}
