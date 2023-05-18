package com.chasing1020.core.idu

import chisel3._
import chisel3.util._
import com.chasing1020.core.{CoreBundle, CoreModule}

class ImmGenIO extends CoreBundle {
  val inst = Input(UInt(VAddrBits.W))
  val instType = Input(UInt(3.W))
  val imm = Output(UInt(XLen.W))
}

class ImmGen extends CoreModule {
  val io = IO(new ImmGenIO)

  val inst = io.inst
  io.imm := MuxLookup(
    key = io.instType,
    default = 0.U,
    mapping = List(
      InstType.I -> Cat(Fill(20, inst(31)), inst(31, 20)),
      InstType.S -> Cat(Fill(20, inst(31)), inst(31, 25), inst(11, 7)),
      InstType.B -> Cat(Fill(20, inst(31)), inst(7), inst(30, 25), inst(11, 8), 0.U(1.W)),
      InstType.U -> Cat(inst(31, 12), 0.U(12.W)),
      InstType.J -> Cat(Fill(12, inst(31)), inst(19, 12), inst(20), inst(30, 21), 0.U(1.W)),
    ),
  )
}

object ImmGen {
  def apply(inst: UInt, instType: UInt): UInt = {
    val immGen = Module(new ImmGen)
    immGen.io.inst := inst
    immGen.io.instType := instType
    immGen.io.imm
  }
}
