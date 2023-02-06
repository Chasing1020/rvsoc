package core

import chisel3._

class AdderIO(width: Int) extends Bundle {
  val a = Input(SInt(width.W))
  val b = Input(SInt(width.W))
  val out = Output(SInt((width + 1).W))
}

class Adder(width: Int) extends Module {
  val io = IO(new AdderIO(width))
  io.out := io.a + io.b
}
