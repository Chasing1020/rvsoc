package core

import chisel3._
import chisel3.util._

class RegFileReadIO extends CoreBundle {
  val addr = Input(UInt(log2Ceil(RegNum).W))
  val data = Output(UInt(XLen.W))
}

class RegFileWriteOut extends CoreBundle {
  val en = Output(Bool())
  val addr = Output(UInt(log2Ceil(RegNum).W))
  val data = Output(UInt(XLen.W))
}

class RegFileIO extends CoreBundle {
  val r1 = new RegFileReadIO
  val r2 = new RegFileReadIO
  val w  = Flipped(new RegFileWriteOut)
}

class RegFile extends CoreModule {
  val io = IO(new RegFileIO)

  val regs = Mem(RegNum, UInt(XLen.W))
  io.r1.data := regs(io.r1.addr)
  io.r2.data := regs(io.r2.addr)
  when(io.w.en & io.w.addr.orR) {
    regs(io.w.addr) := io.w.data
  }
//  (1 until RegNum).foreach(i => printf(cf"\tRegFile.x$i%02d: ${regs(i.U)}\n"))
}
