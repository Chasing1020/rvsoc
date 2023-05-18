package com.chasing1020.memory

import chisel3._
import chisel3.util.Cat
import com.chasing1020.utils._

class EndianReverserIO(width: Int = 32) extends Bundle {
  val in = Input(UInt(width.W))
  val out = Output(UInt(width.W))

  require(width % 8 == 0, "Error: EndianReverserIO width must be divisible by 8")
}

class EndianReverser(width: Int = 32) extends Module {
  val io = IO(new EndianReverserIO(width))

  val reversedBytes = (0 until width / 8).map(i => io.in((i + 1) * 8 - 1, i * 8))
  io.out := Cat(reversedBytes).asUInt

  Trace(cf"[EndianReverser.in ]: ${io.in}%x")
  Trace(cf"[EndianReverser.out]: ${io.out}%x")
}

object EndianReverser {
  def apply(in: UInt, width: Int = 32) = {
    val e = Module(new EndianReverser(width))
    e.io.in := in
    e.io.out
  }

  def main(args: Array[String]): Unit = {
    EmitVerilog(new EndianReverser)
  }
}
