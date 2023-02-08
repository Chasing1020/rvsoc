package memory

import chisel3._
import chisel3.util._
import core._

// Read & Write address channel
class AXI4LiteBundleA extends AXI4Bundle {
  val addr = Output(UInt(PAddrBits.W))
  val prot = Output(UInt(ProtBits.W))
}

// Write data channel
class AXI4LiteBundleW extends AXI4Bundle {
  val strb = Output(UInt((DataBits / 8).W))
  val data = Output(UInt(DataBits.W))
}

// Write response channel
class AXI4LiteBundleB extends AXI4Bundle {
  val resp = Output(UInt(RespBits.W))
}

// Read data channel
class AXI4LiteBundleR extends AXI4Bundle {
  val resp = Output(UInt(RespBits.W))
  val data = Output(UInt(DataBits.W))
}

class AXI4Lite extends Bundle {
  val aw = Decoupled(new AXI4LiteBundleA)
  val w = Decoupled(new AXI4LiteBundleW)
  val b = Flipped(Decoupled(new AXI4LiteBundleB))
  val ar = Decoupled(new AXI4LiteBundleA)
  val r = Flipped(Decoupled(new AXI4LiteBundleR))
}

class AXI4Memory extends CoreModule with AXI4Spec {
  val io = IO(new AXI4Lite)

}

// todo: convert to AMBA AXI and ACE Protocol Specification
class MemIO extends CoreBundle {
  val readEnable = Input(Bool())
  val readAddr = Input(UInt(log2Ceil(XLen).W))
  val dataOut = Output(UInt(XLen.W))

  val writeEnable = Input(Bool())
  val writeAddr = Input(UInt(log2Ceil(XLen).W))
  val dataIn = Input(UInt(XLen.W))
}

class Memory extends CoreModule {
  val io = IO(new MemIO)

  val mem = SyncReadMem(MemBytes, UInt(XLen.W))

  when(io.writeEnable) {
    mem.write(io.writeAddr, io.dataIn)
  }
  io.dataOut := mem.read(io.readAddr, io.readEnable)
}
