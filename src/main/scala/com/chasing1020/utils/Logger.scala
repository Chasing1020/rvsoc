package com.chasing1020.utils

import chisel3._
import Logger._
import Level.Level

object Level extends Enumeration {
  type Level = Value
  // format: off
  final val Trace = Value(0, Console.WHITE   + "TRACE" + Console.RESET)
  final val Debug = Value(1, Console.BLUE    + "DEBUG" + Console.RESET)
  final val Info  = Value(2, Console.GREEN   + "INFO " + Console.RESET)
  final val Warn  = Value(3, Console.YELLOW  + "WARN " + Console.RESET)
  final val Error = Value(4, Console.MAGENTA + "ERROR" + Console.RESET)
  final val Panic = Value(5, Console.RED     + "PANIC" + Console.RESET)
  // format: on
  final val Never = Value(6)
}

object Clock {
  def apply() = {
    val c = RegInit(0.U(32.W))
    c := c + 1.U
    c
  }
}

object Logger {
  var level = Level.Debug
  var useLevelText = false
  var useClock = true
}

// Logger will print the debug message every cycle.
abstract class Logger(l: Level) {
  def apply(cond: => Bool, p:   => Printable): Unit = when(cond) { apply(p) }
  def apply(cond: => Bool, fmt: => String, data: Bits*): Unit = when(cond) { apply(Printable.pack(fmt, data: _*)) }

  def apply(fmt: => String, data: Bits*): Unit = apply(Printable.pack(fmt, data: _*))
  def apply(p: => Printable): Unit = {
    if (l < level) return
    var str = Printable.pack("")
    if (useLevelText) str += l.toString
    if (useClock) str += cf"[${Clock()}] "
    str += p
    if (p.toString.last != '\n') str += cf"\n"
    printf(cf"$str")
  }
}

object Trace extends Logger(Level.Trace)
object Debug extends Logger(Level.Debug)
object Info extends Logger(Level.Info)
object Warn extends Logger(Level.Warn)
object Error extends Logger(Level.Error)
object Panic extends Logger(Level.Panic)
