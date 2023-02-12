package core.idu

import chisel3._
import chisel3.util._
import chisel3.util.experimental.decode._
import core.fu.AluOp
import core.idu.Decoder._
import core.idu.Instruction._

case object FuName {
  final val Unknown: UInt = "b00".U
  final val Alu:     UInt = "b01".U
  final val Bru:     UInt = "b10".U
  final val Lsu:     UInt = "b11".U
}

case object BruOp {
  final val Unknown: UInt = "b0000".U
  final val Jal:     UInt = "b0001".U
  final val Jalr:    UInt = "b0010".U
  final val Beq:     UInt = "b0000".U
  final val Bne:     UInt = "b0001".U
  final val Blt:     UInt = "b0100".U
  final val Bge:     UInt = "b0101".U
  final val Bltu:    UInt = "b0110".U
  final val Bgeu:    UInt = "b0111".U
}

class DecoderIO extends Bundle {
  val inst = Input(UInt(32.W))
  val instType = Output(UInt(4.W))
  val fuName = Output(UInt(4.W))
  val opType = Output(UInt(4.W))
}

class Decoder extends Module {
  val io = IO(new DecoderIO)

  val instructions = rules.map(_._1)

  // todo: finish qmc
//  val table: Iterable[(BitPat, BitPat)] = instructions.zipWithIndex.map { i => (i._1, BitPat(i._2.U(3.W))) }
//  val index = decoder(io.inst, TruthTable(table, default = BitPat(0.U(3.W))))
//  val instType :: fuName :: opType :: Nil = rules(index.litValue.toInt)._2

  val instType :: fuName :: opType :: Nil = ListLookup(io.inst, default, rules)
  io.instType := instType
  io.fuName := fuName
  io.opType := opType
}

object Decoder {
  val default = List(InstType.Unknown, FuName.Unknown, AluOp.Unknown)

  // todo: finish all instructions
  // format: off
  val rules = Array(
    LUI   -> List(InstType.U, FuName.Alu, AluOp.CopyB),
    AUIPC -> List(InstType.U, FuName.Alu, AluOp.Add  ),
    JAL   -> List(InstType.J, FuName.Bru, BruOp.Jal  ),
    JALR  -> List(InstType.I, FuName.Bru, BruOp.Jalr ),
    BEQ   -> List(InstType.B, FuName.Bru, BruOp.Beq  ),
    BNE   -> List(InstType.B, FuName.Bru, BruOp.Bne  ),
    BLT   -> List(InstType.B, FuName.Bru, BruOp.Blt  ),
    BGE   -> List(InstType.B, FuName.Bru, BruOp.Bge  ),
    BLTU  -> List(InstType.B, FuName.Bru, BruOp.Bltu ),
    BGEU  -> List(InstType.B, FuName.Bru, BruOp.Bgeu ),
    ADDI  -> List(InstType.I, FuName.Alu, AluOp.Add  ),
  )
  // format: on

  def apply(inst: UInt) = {
    val d = Module(new Decoder)
    d.io.inst := inst
    List(d.io.instType, d.io.fuName, d.io.opType)
  }
}
