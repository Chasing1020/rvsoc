package com.chasing1020.memory

import chisel3._
import chisel3.util._
import chisel3.util.experimental.loadMemoryFromFileInline
import com.chasing1020.utils._

// Read & Write address channel
class AXI4LiteBundleA extends AXI4Bundle {
  val addr = Output(UInt(PAddrBits.W))
  val prot = Output(UInt(ProtBits.W))
}

// Write data channel
class AXI4LiteBundleW extends AXI4Bundle {
  val strb = Output(UInt(BeatBytes.W))
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

class AXI4LiteIO extends Bundle {
  val aw = Decoupled(new AXI4LiteBundleA)
  val w = Decoupled(new AXI4LiteBundleW)
  val b = Flipped(Decoupled(new AXI4LiteBundleB))
  val ar = Decoupled(new AXI4LiteBundleA)
  val r = Flipped(Decoupled(new AXI4LiteBundleR))
}

class AXI4Memory(filePath: String = "") extends AXI4Module {
  val io = IO(Flipped(new AXI4LiteIO))

  io <> DontCare
  val mem = Mem(1024, UInt(DataBits.W))
  if (filePath.trim().nonEmpty) {
    loadMemoryFromFileInline(mem, filePath)
  }
  // todo: add DecoupledIO fire
  val read = EndianReverser(mem((io.ar.bits.addr >> log2Ceil(BeatBytes).U).asUInt), DataBits)
  io.r.bits.data := read
  io.r.bits.resp := RespOkay
  io.r.valid := true.B

  val write = (0 until BeatBytes).foldLeft(0.U(DataBits.W)) { (write, i) =>
    write | (Mux(
      io.w.bits.strb.asBools(i),
      io.w.bits.data,
      read,
    )(8 * (i + 1) - 1, 8 * i) << (8 * i).U).asUInt
  }
  when(io.w.valid && io.aw.valid) {
    mem.write((io.aw.bits.addr >> log2Ceil(BeatBytes).U).asUInt, EndianReverser(write, DataBits))
  }

//  Debug(cf"[AXI4Memory.aw]: ${io.aw}")
//  Debug(cf"[AXI4Memory.w ]: ${io.w}")
//  Debug(cf"[AXI4Memory.b ]: ${io.b}")
//  Debug(cf"[AXI4Memory.ar]: ${io.ar}")
//  Debug(cf"[AXI4Memory.r ]: ${io.r}")
}

object AXI4Memory {
  def main(args: Array[String]): Unit = {
    EmitVerilog(new AXI4Memory)
  }
}