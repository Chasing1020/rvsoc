import chisel3._
import core.ifu._
import core.idu._
import core.exu._
import core.wbu._
import core.RegFile
import memory._

class Top extends Module {
  val io = IO(new Bundle {
    val mem = new AXI4LiteIO
  })

  val ifu = Module(new Ifu)
  val idu = Module(new Idu)
  val exu = Module(new Exu)
  val wbu = Module(new Wbu)
  val rf = Module(new RegFile)

  io.mem <> ifu.io.mem
  idu.io.in <> ifu.io.out
  rf.io <> idu.io.rf // fixme: rebuild the Idu.io.rf and Wbu.io.rf to avoid re-connect
  exu.io.in <> idu.io.out
  wbu.io.in <> exu.io.out
  rf.io <> wbu.io.rf // fixme: rebuild the Idu.io.rf and Wbu.io.rf to avoid re-connect
  ifu.io.in <> wbu.io.out
}
