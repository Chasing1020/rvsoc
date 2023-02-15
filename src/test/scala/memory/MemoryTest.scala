package memory

import chisel3._
import chisel3.util.experimental.loadMemoryFromFileInline
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import utils._

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
    when(io.write) { rdwrPort := io.dataIn }.otherwise { io.dataOut := rdwrPort }
  }
  stop()
  Debug(cf"${mem(0.U)}%x")
}

class MemoryTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior.of("Memory")

  it should "pass" in {
    test(new InitMemInline("./tests/hex/addi.hex")).runUntilStop()
  }
}
