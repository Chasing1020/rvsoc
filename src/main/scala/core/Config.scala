package core

import chisel3._

trait Config {
  final val XLen:   Int = 64
  final val RegNum: Int = 32

  final val MemBase: Int = 0x8000_0000
  final val MemSize: Int = 8 * 1024 * 1024

  final val ResetVector: Int = MemBase

  final val VAddrBits: Int = 32
}

abstract class CfgModule extends Module with Config
abstract class CfgBundle extends Bundle with Config
