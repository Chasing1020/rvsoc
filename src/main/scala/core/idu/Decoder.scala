package core.idu

import chisel3._
import chisel3.util._
import core.exu.fu.AluOp
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
    BGEU  -> List(InstType.B, FuName.Bru, BruOp.Bgeu ), // todo: LSU
    ADDI  -> List(InstType.I, FuName.Alu, AluOp.Add  ),
    SLTI  -> List(InstType.I, FuName.Alu, AluOp.Slt  ),
    SLTIU -> List(InstType.I, FuName.Alu, AluOp.Sltu ),
    XORI  -> List(InstType.I, FuName.Alu, AluOp.Xor  ),
    ORI   -> List(InstType.I, FuName.Alu, AluOp.Or   ),
    ANDI  -> List(InstType.I, FuName.Alu, AluOp.And  ),
    SLLI  -> List(InstType.I, FuName.Alu, AluOp.Sll  ),
    SRLI  -> List(InstType.I, FuName.Alu, AluOp.Srl  ),
    SRAI  -> List(InstType.I, FuName.Alu, AluOp.Sra  ),
    ADD   -> List(InstType.R, FuName.Alu, AluOp.Add  ),
    SUB   -> List(InstType.R, FuName.Alu, AluOp.Sub  ),
    SLL   -> List(InstType.R, FuName.Alu, AluOp.Sll  ),
    SLT   -> List(InstType.R, FuName.Alu, AluOp.Slt  ),
    SLTU  -> List(InstType.R, FuName.Alu, AluOp.Sltu ),
    XOR   -> List(InstType.R, FuName.Alu, AluOp.Xor  ),
    SRL   -> List(InstType.R, FuName.Alu, AluOp.Srl  ),
    SRA   -> List(InstType.R, FuName.Alu, AluOp.Sra  ),
    OR    -> List(InstType.R, FuName.Alu, AluOp.Or   ),
    AND   -> List(InstType.R, FuName.Alu, AluOp.And  ), // todo: FENCE, ECALL, EBREAK
    ADDIW -> List(InstType.I, FuName.Alu, AluOp.Add  ),
    SLLIW -> List(InstType.I, FuName.Alu, AluOp.Sll  ),
    SRLIW -> List(InstType.I, FuName.Alu, AluOp.Srl  ),
    SRAIW -> List(InstType.I, FuName.Alu, AluOp.Sra  ),
    SLLW  -> List(InstType.R, FuName.Alu, AluOp.Sll  ),
    SRLW  -> List(InstType.R, FuName.Alu, AluOp.Srl  ),
    SRAW  -> List(InstType.R, FuName.Alu, AluOp.Sra  ),
    ADDW  -> List(InstType.R, FuName.Alu, AluOp.Add  ),
    SUBW  -> List(InstType.R, FuName.Alu, AluOp.Sub  ),
  )
  // format: on

  def apply(inst: UInt) = {
    val d = Module(new Decoder)
    d.io.inst := inst
    List(d.io.instType, d.io.fuName, d.io.opType)
  }
}
