package core

import chisel3._
import chisel3.util.log2Ceil

trait CoreConfig {
  final val XLen:   Int = 64
  final val RegNum: Int = 32

  final val MemBase:  Int = 0x8000_0000
  final val MemBytes: Int = 8 * 1024 * 1024

  final val ResetVector: Int = MemBase

  final val VAddrBits: Int = 32
  final val PAddrBits: Int = 32
}

trait CacheConfig extends CoreConfig {
  final val WayNum: Int = 1
  final val SetNum: Int = 256

  final val BlockBytes: Int = 4 * (XLen / 8)
  final val BlockBits:  Int = 4 * (XLen / 8) << 3

  final val BlockLen: Int = log2Ceil(BlockBytes)
  final val SetLen:   Int = log2Ceil(SetNum)
  final val TagLen:   Int = XLen - SetLen - BlockLen
}

abstract class CoreModule extends Module with CoreConfig
abstract class CoreBundle extends Bundle with CoreConfig

abstract class CacheModule extends Module with CacheConfig
abstract class CacheBundle extends Bundle with CacheConfig
