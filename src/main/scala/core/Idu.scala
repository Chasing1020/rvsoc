package core

import chisel3._

class ControlIO extends CfgBundle {
  val inst = Input(UInt(VAddrBits.W))
  val pc = Input(UInt(VAddrBits.W))

}

class Idu extends CfgModule {
  val io = IO(new ControlIO)

}
