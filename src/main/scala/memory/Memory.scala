package memory

import chisel3._
import chisel3.util._
import chisel3.util.experimental.loadMemoryFromFileInline
import core._
import firrtl.annotations.MemoryLoadFileType
import utils.Debug

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

class AXI4Memory(filePath: String = "") extends AXI4Module {
  val io = IO(Flipped(new AXI4Lite))

  io := DontCare
  val mem = SyncReadMem(1024, UInt((BeatBytes * 8).W))
  if (filePath.trim().nonEmpty) {
    loadMemoryFromFileInline(mem, filePath)
  }
  // todo: add DecoupledIO fire
  // fixme: wrong AXI4Memory.ar result
  val read = mem.read(io.ar.bits.addr, true.B).asUInt
  io.r.bits.data := EndianReverser(BeatBytes * 8, read)
//  io.r.bits.data := read
  io.r.bits.resp := RespOkay
  io.r.valid := true.B

//  Debug(cf"[AXI4Memory.aw]: ${io.aw.bits.addr}%x")
//  Debug(cf"[AXI4Memory.w ]: ${io.w.bits.data}%x")
//  Debug(cf"[AXI4Memory.b ]: ${io.b.bits.resp}%x")
  Debug(cf"[AXI4Memory.ar]: ${io.ar.bits.addr}%x")
  Debug(cf"[AXI4Memory.r ]: ${io.r.bits.data}%x")
}

// todo: convert to AMBA AXI and ACE Protocol Specification
class MemIO extends CoreBundle {
  val enable = Input(Bool())
  val write = Input(Bool())
  val addr = Input(UInt(log2Ceil(XLen).W))
  val dataIn = Input(UInt(XLen.W))
  val dataOut = Output(UInt(XLen.W))
}

class Memory(filePath: String = "") extends CoreModule {
  val io = IO(new MemIO)

  val mem = SyncReadMem(MemBytes, UInt(XLen.W))
  if (filePath.trim().nonEmpty) {
    loadMemoryFromFileInline(mem, filePath, MemoryLoadFileType.Hex)
  }

  io.dataOut := DontCare
  when(io.enable) {
    when(io.write) { mem(io.addr) := io.dataIn }.otherwise { io.dataOut := mem(io.addr) }
  }
}
