package core.exu

import chisel3._
import chisel3.util.MuxLookup
import core.{BranchIO, CoreBundle, CoreModule}
import core.fu.{Alu, Bru}
import core.idu.{DataPathIO, FuControlIO, FuName, IduIO}

class ExuIO extends CoreBundle {
  val br = Flipped(new BranchIO)
  val data = new DataPathIO
}

class Exu extends CoreModule {
  val io = IO(new Bundle() {
    val in = Flipped(new IduIO)
    val out = new ExuIO
  })

  val rs1 = io.in.data.rs1
  val rs2 = io.in.data.rs2
  val pc = io.in.pc
  val op = io.in.fu.op

  val aluOut = Alu(width = XLen, a = rs1, b = rs2, op = op)
  val bruOut = Bru(rs1 = rs1, rs2 = rs2, op = op, nextPc = pc + 4.U, offset = rs2)

  io.out := DontCare
  io.out.br := bruOut
}
