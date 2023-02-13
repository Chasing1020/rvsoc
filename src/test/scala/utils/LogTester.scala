package utils

import utils.Level._
import Chisel.testers.BasicTester

abstract class LogTester(l: Level) extends BasicTester { Logger.level = l }

class TraceTester extends LogTester(Level.Trace)
class DebugTester extends LogTester(Level.Debug)
class InfoTester extends LogTester(Level.Info)
class WarnTester extends LogTester(Level.Warn)
class ErrorTester extends LogTester(Level.Error)
class PanicTester extends LogTester(Level.Panic)
