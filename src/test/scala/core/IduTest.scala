package core

import chisel3._
import chiseltest._
import core.idu.Idu
import org.scalatest.flatspec.AnyFlatSpec
import Chisel.testers.BasicTester

class IduTester extends BasicTester {
  val dut = Module(new Idu)
  val rf = Module(new RegFile)
  rf.io <> dut.io.rg

  dut.io.in.pc := DontCare
  dut.io.in.inst := "x06400093".U // addi x1, x0, 100

  stop()
  printf("Inst: 0x%x, rs1: 0x%x, rs2: 0x%x\n", dut.io.in.inst, dut.io.data.rs1, dut.io.data.rs2)
}

class IduTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior.of("Idu")

  it should "success" in test(new IduTester).runUntilStop()
}
