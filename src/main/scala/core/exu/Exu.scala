package core.exu

import chisel3._
import core.{BranchIO, CoreModule}
import core.fu.Alu
import core.idu.{DataPathIO, FuControlIO, IduIO}

class Exu extends CoreModule {
  val io = IO(new Bundle() {
    val in = Flipped(new IduIO)
  })


  val aluOut = Alu(width = XLen, io.in.data.rs1, io.in.data.rs2, io.in.fu.op)
}
