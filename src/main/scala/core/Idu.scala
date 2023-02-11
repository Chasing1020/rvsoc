package core

import chisel3._

class ControlIO extends CoreBundle {
  val inst = Input(UInt(VAddrBits.W))
  val pc = Input(UInt(VAddrBits.W))
}

class Idu extends CoreModule {
  val io = IO(new Bundle {
    val in = new ControlIO
    val out = Flipped(new ControlIO)
  })

}
