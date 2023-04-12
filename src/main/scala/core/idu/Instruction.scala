package core.idu

import chisel3._
import chisel3.util._

case object Instruction {
  final val NOP:   UInt = "x00000013".U(32.W) // ADDI  x0, x0, 0
  final val UNIMP: UInt = "xC0001073".U(32.W) // CSRRW x0, cycle, x0
  final val RET:   UInt = "x00008067".U(32.W) // JALR  x0, x1, 0

  // RV32I Base Instruction Set
  final val LUI:    BitPat = BitPat("b?????????????????????????0110111")
  final val AUIPC:  BitPat = BitPat("b?????????????????????????0010111")
  final val JAL:    BitPat = BitPat("b?????????????????????????1101111")
  final val JALR:   BitPat = BitPat("b?????????????????000?????1100111")
  final val BEQ:    BitPat = BitPat("b?????????????????000?????1100011")
  final val BNE:    BitPat = BitPat("b?????????????????001?????1100011")
  final val BLT:    BitPat = BitPat("b?????????????????100?????1100011")
  final val BGE:    BitPat = BitPat("b?????????????????101?????1100011")
  final val BLTU:   BitPat = BitPat("b?????????????????110?????1100011")
  final val BGEU:   BitPat = BitPat("b?????????????????111?????1100011")
  final val LB:     BitPat = BitPat("b?????????????????000?????0000011")
  final val LH:     BitPat = BitPat("b?????????????????001?????0000011")
  final val LW:     BitPat = BitPat("b?????????????????010?????0000011")
  final val LBU:    BitPat = BitPat("b?????????????????100?????0000011")
  final val LHU:    BitPat = BitPat("b?????????????????101?????0000011")
  final val SB:     BitPat = BitPat("b?????????????????000?????0100011")
  final val SH:     BitPat = BitPat("b?????????????????001?????0100011")
  final val SW:     BitPat = BitPat("b?????????????????010?????0100011")
  final val ADDI:   BitPat = BitPat("b?????????????????000?????0010011")
  final val SLTI:   BitPat = BitPat("b?????????????????010?????0010011")
  final val SLTIU:  BitPat = BitPat("b?????????????????011?????0010011")
  final val XORI:   BitPat = BitPat("b?????????????????100?????0010011")
  final val ORI:    BitPat = BitPat("b?????????????????110?????0010011")
  final val ANDI:   BitPat = BitPat("b?????????????????111?????0010011")
  final val SLLI:   BitPat = BitPat("b0000000??????????001?????0010011")
  final val SRLI:   BitPat = BitPat("b0000000??????????101?????0010011")
  final val SRAI:   BitPat = BitPat("b0100000??????????101?????0010011")
  final val ADD:    BitPat = BitPat("b0000000??????????000?????0110011")
  final val SUB:    BitPat = BitPat("b0100000??????????000?????0110011" )
  final val SLL:    BitPat = BitPat("b0000000??????????001?????0110011")
  final val SLT:    BitPat = BitPat("b0000000??????????010?????0110011")
  final val SLTU:   BitPat = BitPat("b0000000??????????011?????0110011")
  final val XOR:    BitPat = BitPat("b0000000??????????100?????0110011")
  final val SRL:    BitPat = BitPat("b0000000??????????101?????0110011")
  final val SRA:    BitPat = BitPat("b0100000??????????101?????0110011")
  final val OR:     BitPat = BitPat("b0000000??????????110?????0110011")
  final val AND:    BitPat = BitPat("b0000000??????????111?????0110011")
  final val ECALL:  BitPat = BitPat("b00110000001000000000000001110011")
  final val EBREAK: BitPat = BitPat("b00000000000100000000000001110011")

  // RV64I Base Instruction Set (in addition to RV32I)
  // todo: LWU, LD, SD
  final val ADDIW: BitPat = BitPat("b?????????????????000?????0011011")
  final val SLLIW: BitPat = BitPat("b0000000??????????001?????0011011")
  final val SRLIW: BitPat = BitPat("b0000000??????????101?????0011011")
  final val SRAIW: BitPat = BitPat("b0100000??????????101?????0011011")
  final val SLLW:  BitPat = BitPat("b0000000??????????001?????0111011")
  final val SRLW:  BitPat = BitPat("b0000000??????????101?????0111011")
  final val SRAW:  BitPat = BitPat("b0100000??????????101?????0111011")
  final val ADDW:  BitPat = BitPat("b0000000??????????000?????0111011")
  final val SUBW:  BitPat = BitPat("b0100000??????????000?????0111011")

  // RV32/RV64 Zicsr Standard Extension
  final val CSRRW:  BitPat = BitPat("b?????????????????001?????1110011")
  final val CSRRWI: BitPat = BitPat("b?????????????????101?????1110011")
  final val CSRRS:  BitPat = BitPat("b?????????????????010?????1110011")
  final val CSRRSI: BitPat = BitPat("b?????????????????110?????1110011")
  final val CSRRC:  BitPat = BitPat("b?????????????????011?????1110011")
  final val CSRRCI: BitPat = BitPat("b?????????????????111?????1110011")

  // Trap-Return Instructions
  final val SRET: BitPat = BitPat("b00010000001000000000000001110011")
  final val MRET: BitPat = BitPat("b00110000001000000000000001110011")
}

case object InstType {
  final val Unknown: UInt = "b000".U
  final val I:       UInt = "b001".U
  final val S:       UInt = "b010".U
  final val B:       UInt = "b011".U
  final val U:       UInt = "b100".U
  final val J:       UInt = "b101".U
  final val R:       UInt = "b110".U
}
