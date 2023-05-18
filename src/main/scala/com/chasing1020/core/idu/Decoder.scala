package com.chasing1020.core.idu

import chisel3._
import chisel3.util._
import Decoder._
import Instruction._
import com.chasing1020.core.exu.fu.{AluOp, BruOp, CsrOp, LsuOp}

case object FuName {
  final val Unknown: UInt = "b000".U
  final val Alu:     UInt = "b001".U
  final val Bru:     UInt = "b010".U
  final val Lsu:     UInt = "b011".U
  final val Csr:     UInt = "b100".U
}

class DecoderIO extends Bundle {
  val inst = Input(UInt(32.W))
  val instType = Output(UInt(4.W))
  val fuName = Output(UInt(4.W))
  val opType = Output(UInt(4.W))
}

class Decoder extends Module {
  val io = IO(new DecoderIO)

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
    // RV32I Base Instruction Set
    LUI    -> List(InstType.U, FuName.Alu, AluOp.CopyB),
    AUIPC  -> List(InstType.U, FuName.Alu, AluOp.Add  ),
    JAL    -> List(InstType.J, FuName.Bru, BruOp.Jal  ),
    JALR   -> List(InstType.I, FuName.Bru, BruOp.Jalr ),
    BEQ    -> List(InstType.B, FuName.Bru, BruOp.Beq  ),
    BNE    -> List(InstType.B, FuName.Bru, BruOp.Bne  ),
    BLT    -> List(InstType.B, FuName.Bru, BruOp.Blt  ),
    BGE    -> List(InstType.B, FuName.Bru, BruOp.Bge  ),
    BLTU   -> List(InstType.B, FuName.Bru, BruOp.Bltu ),
    BGEU   -> List(InstType.B, FuName.Bru, BruOp.Bgeu ),
    LB     -> List(InstType.I, FuName.Lsu, LsuOp.Lb   ),
    LH     -> List(InstType.I, FuName.Lsu, LsuOp.Lh   ),
    LW     -> List(InstType.I, FuName.Lsu, LsuOp.Lw   ),
    LBU    -> List(InstType.I, FuName.Lsu, LsuOp.Lbu  ),
    LHU    -> List(InstType.I, FuName.Lsu, LsuOp.Lhu  ),
    SB     -> List(InstType.S, FuName.Lsu, LsuOp.Sb   ),
    SH     -> List(InstType.S, FuName.Lsu, LsuOp.Sh   ),
    SW     -> List(InstType.S, FuName.Lsu, LsuOp.Sw   ),
    ADDI   -> List(InstType.I, FuName.Alu, AluOp.Add  ),
    SLTI   -> List(InstType.I, FuName.Alu, AluOp.Slt  ),
    SLTIU  -> List(InstType.I, FuName.Alu, AluOp.Sltu ),
    XORI   -> List(InstType.I, FuName.Alu, AluOp.Xor  ),
    ORI    -> List(InstType.I, FuName.Alu, AluOp.Or   ),
    ANDI   -> List(InstType.I, FuName.Alu, AluOp.And  ),
    SLLI   -> List(InstType.I, FuName.Alu, AluOp.Sll  ),
    SRLI   -> List(InstType.I, FuName.Alu, AluOp.Srl  ),
    SRAI   -> List(InstType.I, FuName.Alu, AluOp.Sra  ),
    ADD    -> List(InstType.R, FuName.Alu, AluOp.Add  ),
    SUB    -> List(InstType.R, FuName.Alu, AluOp.Sub  ),
    SLL    -> List(InstType.R, FuName.Alu, AluOp.Sll  ),
    SLT    -> List(InstType.R, FuName.Alu, AluOp.Slt  ),
    SLTU   -> List(InstType.R, FuName.Alu, AluOp.Sltu ),
    XOR    -> List(InstType.R, FuName.Alu, AluOp.Xor  ),
    SRL    -> List(InstType.R, FuName.Alu, AluOp.Srl  ),
    SRA    -> List(InstType.R, FuName.Alu, AluOp.Sra  ),
    OR     -> List(InstType.R, FuName.Alu, AluOp.Or   ),
    AND    -> List(InstType.R, FuName.Alu, AluOp.And  ),
    ECALL  -> List(InstType.I, FuName.Csr, CsrOp.Jmp  ),
    EBREAK -> List(InstType.I, FuName.Csr, CsrOp.Jmp  ),
    // todo: FENCE

    // RV64I Base Instruction Set (in addition to RV32I)
    // todo: LWU, LD, SD
    ADDIW -> List(InstType.I, FuName.Alu, AluOp.Add  ),
    SLLIW -> List(InstType.I, FuName.Alu, AluOp.Sll  ),
    SRLIW -> List(InstType.I, FuName.Alu, AluOp.Srl  ),
    SRAIW -> List(InstType.I, FuName.Alu, AluOp.Sra  ),
    SLLW  -> List(InstType.R, FuName.Alu, AluOp.Sll  ),
    SRLW  -> List(InstType.R, FuName.Alu, AluOp.Srl  ),
    SRAW  -> List(InstType.R, FuName.Alu, AluOp.Sra  ),
    ADDW  -> List(InstType.R, FuName.Alu, AluOp.Add  ),
    SUBW  -> List(InstType.R, FuName.Alu, AluOp.Sub  ),
    
    // RV32/RV64 Zicsr Standard Extension
    CSRRW -> List(InstType.I, FuName.Csr, CsrOp.Wrt),
    CSRRS -> List(InstType.I, FuName.Csr, CsrOp.Set),
    
    // Trap-Return Instructions
    SRET  -> List(InstType.I, FuName.Csr, CsrOp.Jmp),
    MRET  -> List(InstType.I, FuName.Csr, CsrOp.Jmp),
  )
  // format: on

  def apply(inst: UInt) = {
    val d = Module(new Decoder)
    d.io.inst := inst
    List(d.io.instType, d.io.fuName, d.io.opType)
  }
}
