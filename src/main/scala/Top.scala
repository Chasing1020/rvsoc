import chisel3._
import core.ifu._
import core.idu._
import core.exu._
import core.wbu._
import core.RegFile
import memory._

class Top extends Module {
  val io = IO(new Bundle {
    val imem = new AXI4LiteIO
    val dmem = new AXI4LiteIO
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
}
