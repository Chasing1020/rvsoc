package memory

import chisel3._
import chisel3.util.experimental.loadMemoryFromFileInline
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class InitMemInline(memoryFile: String = "") extends Module {
  val width: Int = 32
  val io = IO(new Bundle {
    val enable = Input(Bool())
    val write = Input(Bool())
    val addr = Input(UInt(10.W))
    val dataIn = Input(UInt(width.W))
    val dataOut = Output(UInt(width.W))
  })

  val mem = SyncReadMem(1024, UInt(width.W))
  // Initialize memory
  if (memoryFile.trim().nonEmpty) {
    loadMemoryFromFileInline(mem, memoryFile)
  }
  io.dataOut := DontCare
  when(io.enable) {
    val rdwrPort = mem(io.addr)
    when (io.write) { rdwrPort := io.dataIn }
      .otherwise    { io.dataOut := rdwrPort }
  }
}

class MemoryTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior.of("Memory")

  it should "success" in {
    test(new Memory){dut =>
//      dut.io.addr.poke("x29a".U)
//      dut.io.enable.poke(true.B)
//      dut.io.dataOut.expect(1.U)
//      dut.io.writeEnable.poke(true.B)
//      dut.io.writeAddr.poke(1.U)
//      dut.io.dataIn.poke(6.U)
//      dut.clock.step(1)
//
//      dut.io.writeEnable.poke(true.B)
//      dut.io.writeAddr.poke(2.U)
//      dut.io.dataIn.poke(7.U)
//      dut.clock.step(1)
//
//      dut.io.readEnable.poke(true.B)
//      dut.io.readAddr.poke(1.U)
//      dut.clock.step(1)
//      dut.io.dataOut.expect(6.U)
//
//      dut.io.readEnable.poke(true.B)
//      dut.io.readAddr.poke(2.U)
//      dut.clock.step(1)
//      dut.io.dataOut.expect(7.U)
    }
  }
}
