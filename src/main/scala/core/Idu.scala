package core

import chisel3._

class ControlIO extends CoreBundle {
  val inst = Input(UInt(VAddrBits.W))
  val pc = Input(UInt(VAddrBits.W))
}

class Idu extends CoreModule {
  val io = IO(new Bundle {
    val in = new ControlIO
    val rg = new RegFileIO
    val out = Flipped(new ControlIO)
  })

  val instType :: fuType :: opType :: Nil = Decoder(io.in.inst)
  val imm = ImmGen(io.in.inst, instType)
  val rd_addr = io.in.inst(11, 7)
  val rs1_addr = io.in.inst(19, 15)
  val rs2_addr = io.in.inst(24, 20)


  //  fixme: add regFile in data path
  io.rg.raddr1 := rs1_addr
  io.rg.raddr2 := rs2_addr
}
