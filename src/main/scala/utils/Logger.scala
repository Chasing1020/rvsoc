package utils

import chisel3._
import chisel3.util.Counter
import utils.Logger._
import utils.Level.Level

object Level extends Enumeration {
  type Level = Value
  // format: off
  final val Trace = Value(0, Console.GREEN   + "TRACE" + Console.RESET)
  final val Debug = Value(1, Console.WHITE   + "DEBUG" + Console.RESET)
  final val Info  = Value(2, Console.BLUE    + "INFO " + Console.RESET)
  final val Warn  = Value(3, Console.YELLOW  + "WARN " + Console.RESET)
  final val Error = Value(4, Console.MAGENTA + "ERROR" + Console.RESET)
  final val Panic = Value(5, Console.RED     + "PANIC" + Console.RESET)
  // format: on
}

object Logger {
  var level = Level.Debug
  var useLevelText = true
  var useCounter = true
  private final val c = Counter(true.B, Int.MaxValue)
}

abstract class Logger(l: Level = Level.Debug) {
  def apply(p: Printable): Unit = {
    if (l < level) return
    if (useLevelText) printf(l.toString)
    if (useCounter) printf("[%d] ", c._1)
    printf(p)
    if (p.toString.last != '\n') printf("\n")
  }
}

object Trace extends Logger(Level.Trace)
object Debug extends Logger(Level.Debug)
object Info extends Logger(Level.Info)
object Warn extends Logger(Level.Warn)
object Error extends Logger(Level.Error)
object Panic extends Logger(Level.Panic)
