package core

import chisel3._
import chisel3.util._
import utils._

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
  val w = Flipped(new RegFileWriteOut)
}

class RegFile extends CoreModule {
  val io = IO(new RegFileIO)

  val regs = Mem(RegNum, UInt(XLen.W))
  io.r1.data := regs.read(io.r1.addr)
  io.r2.data := regs.read(io.r2.addr)
  when(io.w.en & io.w.addr.orR) {
    regs.write(io.w.addr, io.w.data)
  }

  Info(cf"[RegFile.r]: r1.addr: ${io.r1.addr}, r1.data: ${io.r1.data}; r2.addr: ${io.r2.addr}, r2.data: ${io.r2.data}")
  Info(cf"[RegFile.w]: w.en: ${io.w.en} w.addr: ${io.w.addr}, w.data: ${io.w.data}")
}

object RegFile {
  final val AbiMap = Seq(
    "zero",
    "ra",
    "sp",
    "gp",
    "tp",
    "t0",
    "t1",
    "t2",
    "s0",
    "s1",
    "a0",
    "a1",
    "a2",
    "a3",
    "a4",
    "a5",
    "a6",
    "a7",
    "s2",
    "s3",
    "s4",
    "s5",
    "s6",
    "s7",
    "s8",
    "s9",
    "s10",
    "s11",
    "t3",
    "t4",
    "t5",
    "t6"
  )
}
