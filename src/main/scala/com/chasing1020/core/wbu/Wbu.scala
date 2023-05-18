package com.chasing1020.core.wbu

import chisel3._
import com.chasing1020.core.{CoreModule, RegFileWriteOut}
import com.chasing1020.core.exu.ExuOut
import com.chasing1020.core.exu.fu.BranchOut
import com.chasing1020.utils.Trace
import com.chasing1020.core.RegFileIO
import com.chasing1020.utils._

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
