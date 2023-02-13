package utils

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import chisel3.util.Counter

class LoggerTester extends TraceTester {
  val (i, done) = Counter(true.B, 5)

  Trace("trace text")
  Debug("debug text")
  Info("info text")
  Warn("warn text")
  Error("error text")
  Panic("panic text")

  when(done) { stop() }
}

class LoggerTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior.of("LogTest")

  it should "succeed" in { test(new LoggerTester) }.runUntilStop()
}
