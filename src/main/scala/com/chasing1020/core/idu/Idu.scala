package com.chasing1020.core.idu

import chisel3._
import com.chasing1020.core.{CoreBundle, CoreModule, RegFileReadIO, RegFileWriteOut}
import com.chasing1020.utils.{Debug, Trace}
import com.chasing1020.core._
import com.chasing1020.utils._

class InstPcOut extends CoreBundle {
  val inst = Output(UInt(VAddrBits.W))
  val pc = Output(UInt(VAddrBits.W))
}

class DataPathOut extends CoreBundle {
  val rs1 = Output(UInt(XLen.W))
  val rs2 = Output(UInt(XLen.W))
  val offset = Output(UInt(XLen.W)) //
}

class FuControlOut extends Bundle {
  val name = Output(UInt(4.W))
  val op = Output(UInt(4.W))
}

class IduOut extends CoreBundle {
  val pc = Output(UInt(32.W))
  val data = new DataPathOut
  val fc = new FuControlOut
  val rfw = new RegFileWriteOut
}

class IduIO extends Bundle {
  val in = Flipped(new InstPcOut)
  val rfr1 = Flipped(new RegFileReadIO)
  val rfr2 = Flipped(new RegFileReadIO)
  val out = new IduOut
}

class Idu extends CoreModule {
  val io = IO(new IduIO)

  // Mock ECALL as Bubble, then print the value of a1
  val isEbreak = io.in.inst === Instruction.EBREAK
  val inst = Mux(isEbreak, Instruction.NOP, io.in.inst)

  val instType :: fuName :: opType :: Nil = Decoder(inst)
  val imm = ImmGen(inst, instType)

  //  fixme: add regFile in data path
  io.rfr1.addr := Mux(isEbreak, 11.U, inst(19, 15))
  io.rfr2.addr := inst(24, 20)

  val rs1 :: rs2 :: Nil = MuxList(
    addr = instType,
    default = List(0.U, 0.U),
    mapping = Array(
      InstType.I -> List(io.rfr1.data, imm), // rd = rs1 op imm; rd = PC+4, PC = rs1 + imm
      InstType.S -> List(io.rfr1.data, io.rfr2.data), // M[rs1+imm][0:x] = rs2[0:x]
      InstType.B -> List(io.rfr1.data, io.rfr2.data), // if(rs1 op rs2) PC += imm
      InstType.U -> List(io.in.pc, imm), // rd = PC + (imm << 12)
      InstType.J -> List(io.in.pc, imm), // e.g. rd = PC+4; PC += imm
      InstType.R -> List(io.rfr1.data, io.rfr2.data), // rd = rs1 - rs2
    ),
  )
  io.out.data.rs1 := Mux(isEbreak, 0.U, rs1)
  io.out.data.rs2 := rs2
  io.out.data.offset := imm // for S-type and B-type

  io.out.fc.name := fuName
  io.out.fc.op := opType

  io.out.rfw.data := DontCare
  io.out.rfw.en := VecInit(InstType.I, InstType.U, InstType.R, InstType.J).contains(instType)
  io.out.rfw.addr := inst(11, 7)

  io.out.pc := io.in.pc

  when(isEbreak) {
    printf("%c", rs1)
  }

  Trace(cf"[Idu.in]: ${io.in}")
  Trace(cf"[Idu.out]: ${io.out}")
  Debug("[inst]: %x", io.in.inst)
  Debug("[pc]: %x", io.in.pc)
}
