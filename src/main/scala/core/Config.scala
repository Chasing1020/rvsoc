package core

import chisel3._

trait CoreConfig {
  final val XLen:   Int = 64
  final val RegNum: Int = 32

  final val MemBase: Int = 0x8000_0000
  final val MemSize: Int = 8 * 1024 * 1024

  final val ResetVector: Int = MemBase

  final val VAddrBits: Int = 32
}

trait CacheConfig extends CoreConfig {
  final val WayNum = 1
  final val SetNum = 256
  final val BlockBytes = 4 * (XLen / 8)
  final val BlockBits = 4 * (XLen / 8) << 3
}

abstract class CfgModule extends Module with CoreConfig
abstract class CfgBundle extends Bundle with CoreConfig
