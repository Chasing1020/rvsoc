package core

import chisel3._
import core.exu.ExuOut
import core.fu.BranchOut

class Wbu extends CoreModule {
  val io = IO(new Bundle() {
    val in = Flipped(new ExuOut)
    val rg = Flipped(new RegFileIO)
    val out = new BranchOut
  })

//  when(io.in.rfw.en) {
//    io.rg.w.addr := io.in.rfw.addr
//    io.rg.w.addr := io.in.rfw.data
//  }
  io.rg.w <> io.in.rfw
  io.out <> io.in.br
}
