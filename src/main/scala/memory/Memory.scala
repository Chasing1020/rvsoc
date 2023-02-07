package memory

import chisel3._
import chisel3.util.log2Ceil
import core.{CfgBundle, CfgModule}

class MemIO extends CfgBundle {
  val readEnable = Input(Bool())
  val readAddr = Input(UInt(log2Ceil(XLen).W))
  val dataOut = Output(UInt(XLen.W))

  val writeEnable = Input(Bool())
  val writeAddr = Input(UInt(log2Ceil(XLen).W))
  val dataIn = Input(UInt(XLen.W))
}

class Memory extends CfgModule {
  val io = IO(new MemIO)

  val mem = SyncReadMem(MemSize, UInt(XLen.W))

  when (io.writeEnable) {
    mem.write(io.writeAddr, io.dataIn)
  }
  io.dataOut := mem.read(io.readAddr, io.readEnable)
}
