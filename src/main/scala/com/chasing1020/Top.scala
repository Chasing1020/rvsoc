package com.chasing1020

import chisel3._
import com.chasing1020.core.RegFile
import com.chasing1020.core.exu.Exu
import com.chasing1020.core.idu.{Idu, Instruction}
import com.chasing1020.core.ifu.Ifu
import com.chasing1020.core.wbu.Wbu
import com.chasing1020.memory.AXI4LiteIO
import com.chasing1020.utils.Panic

class Top extends Module {
  val io = IO(new Bundle {
    val imem = new AXI4LiteIO
    val dmem = new AXI4LiteIO
    val exit = Output(Bool())
  })

  val ifu = Module(new Ifu)
  val idu = Module(new Idu)
  val exu = Module(new Exu)
  val wbu = Module(new Wbu)
  val rf = Module(new RegFile)

  io.imem <> ifu.io.imem
  idu.io.in <> ifu.io.out
  rf.io.r1 <> idu.io.rfr1
  rf.io.r2 <> idu.io.rfr2
  exu.io.in <> idu.io.out
  io.dmem <> exu.io.dmem
  wbu.io.in <> exu.io.out
  rf.io.w <> wbu.io.rfw
  ifu.io.in <> wbu.io.out

  io.exit := (ifu.io.out.inst === Instruction.UNIMP) || (ifu.io.out.inst === Instruction.RET && ifu.io.out.pc === 4.U)
  Panic(io.exit, "=== Program exit ===")
}
