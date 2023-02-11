package core.idu

import chisel3._
import chisel3.util.{BitPat, ListLookup, MuxLookup}
import core._

class ControlIO extends CoreBundle {
  val inst = Input(UInt(VAddrBits.W))
  val pc = Input(UInt(VAddrBits.W))
}

class DataPathIO extends CoreBundle {
  val rs1 = Output(UInt(XLen.W))
  val rs2 = Output(UInt(XLen.W))
  val dest = Output(UInt(XLen.W))
}

class FuControlIO extends Bundle {
  val name = Output(UInt(4.W))
  val op = Output(UInt(4.W))
}

class Idu extends CoreModule {
  val io = IO(new Bundle {
    val in = new ControlIO
    val rg = Flipped(new RegFileIO)
    val data = new DataPathIO
    val fu = new FuControlIO
  })

  val instType :: fuName :: opType :: Nil = Decoder(io.in.inst)
  val imm = ImmGen(io.in.inst, instType)
  val rd_addr = io.in.inst(11, 7)
  val rs1_addr = io.in.inst(19, 15)
  val rs2_addr = io.in.inst(24, 20)

  //  fixme: add regFile in data path
  io.rg.raddr1 := rs1_addr
  io.rg.raddr2 := rs2_addr
  io.rg.waddr := rd_addr

  val wenSeq = Seq(InstType.I, InstType.U, InstType.R, InstType.J)
  io.rg.wen := MuxLookup(io.in.inst, false.B, wenSeq.zip(Seq.fill(wenSeq.length)(true.B)))

  val rs1 :: rs2 :: Nil = ListLookup(
    addr = instType,
    default = List(0.U(32.W), 0.U(32.W)),
    mapping = Array(
      BitPat(InstType.I) -> List(io.rg.raddr1, imm),
      BitPat(InstType.S) -> List(io.rg.rdata1, imm),
      BitPat(InstType.B) -> List(io.rg.rdata1, imm),
      BitPat(InstType.U) -> List(io.in.pc, imm),
      BitPat(InstType.J) -> List(io.in.pc, imm),
      BitPat(InstType.R) -> List(io.rg.rdata1, io.rg.rdata2)
    )
  )

  io.data.rs1 := rs1
  io.data.rs2 := rs2
  io.fu.name := fuName
  io.fu.op := opType

  io.rg.wdata := DontCare
  io.data.dest := DontCare
}
