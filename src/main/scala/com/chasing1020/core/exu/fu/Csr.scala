package com.chasing1020.core.exu.fu

import chisel3._
import chisel3.util.experimental.decode.decoder
import chisel3.util.{Mux1H, MuxLookup, is, switch}
import com.chasing1020.core.CoreModule
import com.chasing1020.utils.EmitVerilog

case object Mode {
  final val User:       Int = 0
  final val Supervisor: Int = 1
  final val Machine:    Int = 3
}

case object CsrOp {
  final val Unknown: UInt = "b0000".U
  final val Jmp:     UInt = "b0001".U
  final val Wrt:     UInt = "b0010".U
  final val Set:     UInt = "b0011".U
  final val Clr:     UInt = "b0101".U
}

// Volume II: RISC-V Privileged Architectures V20211203 p10
// format: off
trait CsrSpec {
  // Machine information registers
  final val Mvendorid:  UInt = 0xF11.U // Vendor ID
  final val Marchid:    UInt = 0xF12.U // Architecture ID
  final val Mimpid:     UInt = 0xF13.U // Implementation ID
  final val Mhartid:    UInt = 0xF14.U // Hardware thread ID
  final val Mconfigptr: UInt = 0xF15.U // Pointer to configuration data structure

  // Machine Trap Setup
  final val Mstatus:    UInt = 0x300.U // Machine status register
  final val Misa:       UInt = 0x301.U // ISA and extensions
  final val Medeleg:    UInt = 0x302.U // Machine exception delegation register
  final val Mideleg:    UInt = 0x303.U // Machine interrupt delegation register
  final val Mie:        UInt = 0x304.U // Machine interrupt-enable register
  final val Mtvec:      UInt = 0x305.U // Machine trap-handler base address
  final val Mcounteren: UInt = 0x306.U // Machine counter enable
  final val Mstatush:   UInt = 0x310.U // Additional machine status register, RV32 only

  // Machine Trap Handling
  final val Mscratch: UInt = 0x340.U // Scratch register for machine trap handlers
  final val Mepc:     UInt = 0x341.U // Machine exception program counter
  final val Mcause:   UInt = 0x342.U // Machine trap cause
  final val Mtval:    UInt = 0x343.U // Machine bad address or instruction
  final val Mip:      UInt = 0x344.U // Machine interrupt pending
  final val Mtinst:   UInt = 0x34A.U // Machine trap instruction (transformed)
  final val Mtval2:   UInt = 0x34B.U // Machine bad guest physical address

  // Machine Configuration
  final val Menvcfg:  UInt = 0x30A.U // Machine environment configuration register.
  final val Menvcfgh: UInt = 0x31A.U // Additional machine env. conf. register, RV32 only.
  final val Mseccfg:  UInt = 0x747.U // Machine security configuration register.
  final val Mseccfgh: UInt = 0x757.U // Additional machine security conf. register, RV32 only.

  // Machine Counter/Timers
  final val Mcycle:     UInt = 0xB00.U // Machine cycle counter.
  final val Minstret:   UInt = 0xB02.U // Machine instructions-retired counter.
  // val MhpmcounterN:  UInt = 0xB03.U until 0xB1F, Machine performance-monitoring counter.
  final val Mcycleh:    UInt = 0xB80.U // Upper 32 bits of Mcycle, RV32 only.
  final val Minstreth:  UInt = 0xB82.U // Upper 32 bits of Minstret, RV32 only.
  // val MhpmcounterNh: UInt = 0xB83.U until 0xB9F, Upper 32 bits of MhpmcounterN, RV32 only.

  final val priEcall: UInt = 0x000.U
  final val priMret:  UInt = 0x302.U
}
// format: on

class CsrOut extends Bundle {
  val br = new BranchOut
  val data = Output(UInt(32.W))
}

class CsrIO extends Bundle {
  val rs1 = Input(UInt(32.W))
  val rs2 = Input(UInt(32.W))
  val op = Input(UInt(4.W))
  val pc = Input(UInt(32.W))
  val en = Input(Bool())
  val isInvOpcode = Input(Bool())

  val out = new CsrOut
}

class Csr extends CoreModule with CsrSpec {
  val io = IO(new CsrIO)

  val mtvec = Reg(UInt(32.W))
  val mcause = Reg(UInt(32.W))
  val mstatus = Reg(UInt(32.W))
  val mepc = Reg(UInt(32.W))
  val mhpmcounter = List.fill(0x80)(RegInit(0.U(64.W)))

  val addr = io.rs2(11, 0)
  val rdata = MuxLookup(
    key = addr,
    default = 0.U,
    mapping = Seq(
      Mtvec -> mtvec,
      Mcause -> mcause,
      Mepc -> mepc,
      Mstatus -> mstatus,
    ) ++
      (3 to 31).map(i => (0xb00 + i).U -> mhpmcounter(i)) ++ // MhpmcounterN
      (3 to 31).map(i => (0xb80 + i).U -> mhpmcounter(i)(63, 32)), // MhpmcounterNh
  )(XLen - 1, 0)

  val wdata = MuxLookup(
    key = io.op,
    default = 0.U,
    mapping = Seq(
      CsrOp.Wrt -> io.rs1,
      CsrOp.Set -> (rdata | io.rs1),
      CsrOp.Clr -> (rdata & (~io.rs1).asUInt),
    ),
  )

  val isMret = addr === priMret
  val isException = io.isInvOpcode && io.en
  val isEcall = (addr === priEcall) && !isException
  val exceptionNO = Mux1H(
    List(
      io.isInvOpcode -> 2.U,
      isEcall -> 11.U, // 8 + prv mode (3: machine mode)
    ),
  )

  io.out.br.taken := (io.en && io.op === CsrOp.Jmp) || isException
  io.out.br.target := Mux(isMret, mepc, mtvec)
  when(io.out.br.taken && !isMret) {
    mepc := io.pc
    mcause := exceptionNO
  }

  when(io.en && io.op =/= CsrOp.Jmp) {
    switch(addr) {
      is(Mtvec) { mtvec := wdata }
      is(Mstatus) { mstatus := wdata }
      is(Mepc) { mepc := wdata }
      is(Mcause) { mcause := wdata }
    }
  }

  io.out.data := rdata
}

object Csr {
  def apply(en: Bool, rs1: UInt, rs2: UInt, op: UInt, pc: UInt) = {
    val csr = Module(new Csr)
    csr.io.rs1 := rs1
    csr.io.rs2 := rs2
    csr.io.op := op
    csr.io.en := en
    csr.io.pc := pc
    csr.io.isInvOpcode := true.B

    csr.io.out
  }

  def main(args: Array[String]): Unit = {
    EmitVerilog(new Csr)
  }
}
