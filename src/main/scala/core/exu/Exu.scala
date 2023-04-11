package core.exu

import chisel3._
import chisel3.util.MuxLookup
import core.{CoreBundle, CoreModule, RegFileWriteOut}
import core.exu.fu.{Alu, BranchOut, Bru, Csr, Lsu}
import core.idu.{FuName, IduOut}
import memory.AXI4LiteIO
import utils._

class ExuOut extends CoreBundle {
  val br = new BranchOut
  val rfw = new RegFileWriteOut
}

class Exu extends CoreModule {
  val io = IO(new Bundle() {
    val in = Flipped(new IduOut)
    val dmem = new AXI4LiteIO
    val out = new ExuOut
  })

  val rs1 = io.in.data.rs1
  val rs2 = io.in.data.rs2
  val offset = io.in.data.offset
  val pc = io.in.pc
  val op = io.in.fc.op

  val aluOut = Alu(width = XLen, rs1 = rs1, rs2 = rs2, op = op)
  val bruOut = Bru(rs1 = rs1, rs2 = rs2, op = op, pc = pc, offset = offset)
  val lsuOut = Lsu(io.dmem)(en = io.in.fc.name === FuName.Lsu, rs1 = rs1, rs2 = rs2, op = op, offset = offset)
  val csrOut = Csr(en = io.in.fc.name === FuName.Lsu, rs1 = rs1, rs2 = rs2, op = op, pc = io.in.pc)

  io.out.rfw <> io.in.rfw
  io.out.rfw.data := MuxLookup(
    key = io.in.fc.name,
    default = 0.U,
    mapping = List(
      FuName.Alu -> aluOut,
      FuName.Bru -> (pc + 4.U),
      FuName.Lsu -> lsuOut,
    ),
  )
  io.out.br.taken := Mux(io.in.fc.name === FuName.Bru, bruOut.taken, false.B)
  io.out.br.target := bruOut.target

//  Debug(cf"[Exu.in]: ${io.in}")
//  Debug(cf"[Exu.out]: ${io.out}")
//  Debug(cf"[pc]: ${io.in.pc}")
}
