package core

import chisel3._
import chisel3.util._
import chisel3.util.experimental.decode._
import core.Decoder._
import core.Instruction._

case object FuType {
  final val Unknown: UInt = "b00".U
  final val Alu:     UInt = "b01".U
  final val Bru:     UInt = "b10".U
  final val Lsu:     UInt = "b11".U
}

case object BruOp {
  final val Unknown: UInt = "b0000".U
  final val Jal:     UInt = "b0001".U
  final val Jalr:    UInt = "b0010".U
}

class DecoderIO extends Bundle {
  val inst = Input(UInt(32.W))
  val instType = Output(UInt(4.W))
  val fuType = Output(UInt(4.W))
  val opType = Output(UInt(4.W))
}

class Decoder extends Module {
  val io = IO(new DecoderIO)

  val instructions = rules.map(_._1)

  val table = instructions.zipWithIndex.map { i => (i._1, BitPat(i._2.U)) }
  val index = decoder(io.inst, TruthTable(table, default = NOP))
  val instType :: fuType :: opType :: Nil = rules(index.litValue.toInt)._2
  io.instType := instType
  io.fuType := fuType
  io.opType := opType
}

object Decoder {
  val default = List(InstType.Unknown, FuType.Unknown, AluOp.Unknown)

  // todo: finish all instructions
  // format: off
  val rules = Array(
    LUI   -> List(InstType.U, FuType.Alu, AluOp.Lui ),
    AUIPC -> List(InstType.U, FuType.Alu, AluOp.Add ),
    JAL   -> List(InstType.J, FuType.Bru, BruOp.Jal ),
    JALR  -> List(InstType.I, FuType.Bru, BruOp.Jalr),
    ADDI  -> List(InstType.I, FuType.Alu, AluOp.Add ),
  )
  // format: on

  def apply(inst: UInt) = {
    val d = new Decoder
    d.io.inst := inst
    List(d.io.instType, d.io.fuType, d.io.opType)
  }
}
