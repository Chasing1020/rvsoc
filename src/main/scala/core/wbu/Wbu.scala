package core.wbu

import chisel3._
import core.{CoreModule, RegFileIO, RegFileWriteOut}
import core.exu.ExuOut
import core.exu.fu.BranchOut
import utils._

class WbuIO extends Bundle {
  val in = Flipped(new ExuOut)
  val rfw = new RegFileWriteOut
  val out = new BranchOut
}

class Wbu extends CoreModule {
  val io = IO(new WbuIO)

  io.rfw <> io.in.rfw
  io.out <> io.in.br

  Trace(cf"[Wbu.io.rfw] ${io.rfw}")
}
