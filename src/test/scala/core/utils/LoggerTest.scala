package core.utils

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import utils._
import Chisel.testers.BasicTester
import chisel3.util.Counter

class LoggerTester extends BasicTester {
  val (i, done) = Counter(true.B, 5)

  Logger.level = Level.Warn

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
