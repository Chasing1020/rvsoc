package core

import chisel3._
import chisel3.util._
import chisel3.util.experimental.decode._
import core.Decoder._
import core.Instruction._

class DecoderIO extends Bundle {
  val inst = Input(UInt(32.W))
  val out = Output(UInt())
}

class Decoder extends Module {
  val io = IO(new DecoderIO)

  io.out := rules(decoder(io.inst, TruthTable(table, default = NOP)).litValue.toInt)._2
}

object Decoder {
  // todo: add spec rule enum
  val rules = Seq(
    LUI -> 0.U,
    AUIPC -> 1.U,
    JAL -> 2.U,
    JALR -> 3.U,
    ADDI -> 4.U
  )

  val instructions = rules.map(_._1)

  val table: Iterable[(BitPat, BitPat)] = instructions.zipWithIndex.map { i =>
    (i._1, BitPat(i._2.U))
  }

  def apply(inst: UInt) = {
    val d = new Decoder
    d.io.inst := inst
    rules(d.io.out.litValue.toInt)._2
  }
}
