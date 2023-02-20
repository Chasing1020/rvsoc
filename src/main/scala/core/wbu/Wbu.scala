package core.wbu

import chisel3._
import core.{CoreModule, RegFileIO}
import core.exu.ExuOut
import core.exu.fu.BranchOut
import utils._

class WbuIO extends Bundle {
  val in = Flipped(new ExuOut)
  val rf = Flipped(new RegFileIO)
  val out = new BranchOut
}

class Wbu extends CoreModule {
  val io = IO(new WbuIO)

  io.rf.r1 <> DontCare
  io.rf.r2 <> DontCare
  io.rf.w <> io.in.rfw
  io.out <> io.in.br

  Trace(cf"[Wbu.io.rf.w] ${io.rf.w}")
}
