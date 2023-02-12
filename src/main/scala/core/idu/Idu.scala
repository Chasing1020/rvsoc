package core.idu

import chisel3._
import chisel3.util.{BitPat, ListLookup, MuxLookup}
import core._

class ControlOut extends CoreBundle {
  val inst = Output(UInt(VAddrBits.W))
  val pc = Output(UInt(VAddrBits.W))
}

class DataPathOut extends CoreBundle {
  val rs1 = Output(UInt(XLen.W))
  val rs2 = Output(UInt(XLen.W))
  val offset = Output(UInt(XLen.W)) // InstType.S and InstType.B only
  val dest = Output(UInt(XLen.W))
}

class FuControlOut extends Bundle {
  val name = Output(UInt(4.W))
  val op = Output(UInt(4.W))
}

class IduOut extends CoreBundle {
  val pc = Output(UInt(32.W))
  val data = new DataPathOut
  val fu = new FuControlOut
}

class Idu extends CoreModule {
  val io = IO(new Bundle {
    val in = Flipped(new ControlOut)
    val rg = Flipped(new RegFileIO)
    val out = new IduOut
  })

  val instType :: fuName :: opType :: Nil = Decoder(io.in.inst)
  val imm = ImmGen(io.in.inst, instType)

  //  fixme: add regFile in data path
  io.rg.raddr1 := io.in.inst(19, 15)
  io.rg.raddr2 := io.in.inst(24, 20)
  io.rg.waddr := io.in.inst(11, 7)

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

  io.out.data.offset := Mux(instType === InstType.B || instType === InstType.S, imm, 0.U)
  io.out.data.rs1 := rs1
  io.out.data.rs2 := rs2
  io.out.fu.name := fuName
  io.out.fu.op := opType

  io.rg.wdata := DontCare
  io.out.data.dest := DontCare
  io.out.pc := io.in.pc
}
