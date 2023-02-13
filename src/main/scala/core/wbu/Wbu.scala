package core.wbu

import chisel3._
import core.{CoreModule, RegFileIO}
import core.exu.ExuOut
import core.exu.fu.BranchOut

class WbuIO extends Bundle {
  val in = Flipped(new ExuOut)
  val rf = Flipped(new RegFileIO)
  val out = new BranchOut
}

class Wbu extends CoreModule {
  val io = IO(new WbuIO)

  io.rf.r1 <> DontCare
  io.rf.r2 <> DontCare
  io.rf.w.addr := io.in.rfw.addr
  io.rf.w.data := io.in.rfw.data
  io.rf.w.en := io.in.rfw.en
  io.out <> io.in.br
}
