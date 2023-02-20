package utils

import utils.Level._
import Chisel.testers.BasicTester

abstract class LogTester(l: Level) extends BasicTester { Logger.level = l }

abstract class TraceTester extends LogTester(Level.Trace)
abstract class DebugTester extends LogTester(Level.Debug)
abstract class InfoTester extends LogTester(Level.Info)
abstract class WarnTester extends LogTester(Level.Warn)
abstract class ErrorTester extends LogTester(Level.Error)
abstract class PanicTester extends LogTester(Level.Panic)
