package core

import chisel3._

trait Config {
  final val XLen:   Int = 64
  final val RegNum: Int = 32
}

abstract class CfgModule extends Module with Config
abstract class CfgBundle extends Bundle with Config
