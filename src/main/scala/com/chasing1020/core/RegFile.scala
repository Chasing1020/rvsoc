package com.chasing1020.core

import chisel3._
import chisel3.util._
import RegFile.dump
import com.chasing1020.utils._

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

//  Info(cf"[RegFile.r]: r1.addr: ${io.r1.addr}, r1.data: ${io.r1.data}; r2.addr: ${io.r2.addr}, r2.data: ${io.r2.data}")
//  Info(cf"[RegFile.w]: w.en: ${io.w.en} w.addr: ${io.w.addr}, w.data: ${io.w.data}")
  Info(dump(regs))
}

object RegFile extends CoreConfig {
  final val AbiMap = Seq(
    "zero", //  x0
    "  ra", //  x1
    "  sp", //  x2
    "  gp", //  x3
    "  tp", //  x4
    "  t0", //  x5
    "  t1", //  x6
    "  t2", //  x7
    "  s0", //  x8
    "  s1", //  x9
    "  a0", // x10
    "  a1", // x11
    "  a2", // x12
    "  a3", // x13
    "  a4", // x14
    "  a5", // x15
    "  a6", // x16
    "  a7", // x17
    "  s2", // x18
    "  s3", // x19
    "  s4", // x20
    "  s5", // x21
    "  s6", // x22
    "  s7", // x23
    "  s8", // x24
    "  s9", // x25
    " s10", // x26
    " s11", // x27
    "  t3", // x28
    "  t4", // x29
    "  t5", // x30
    "  t6", // x31
  )

  def dump(regs: Mem[UInt], useAbiMap: Boolean = true) =
    (0 until RegNum).foldLeft(Printable.pack("[RegFile.dump]: \n"))((s, i) =>
      s + cf"${if (useAbiMap) AbiMap(i) else "x%02d".format(i)}: ${regs.read(i.U)}"
        + cf"${if (i % 8 != 7) ", " else "\n"}",
    )
}
