package memory

import chisel3._
import core._

// AMBA AXI4-Lite bus protocol
// link: https://developer.arm.com/documentation/ihi0022/e/AMBA-AXI4-Lite-Interface-Specification
trait AXI4Config extends CoreConfig {
  final val DataBits:  Int = 32
  final val BeatBytes: Int = DataBits / 8 // An individual data transfer within an AXI burst

  final val ProtBits: Int = 3
  final val RespBits: Int = 2
}

trait AXI4Spec extends AXI4Config {
  // Protection encoding
  final val ProtPrivileged:  UInt = "b001".U(ProtBits.W)
  final val ProtInsecure:    UInt = "b010".U(ProtBits.W)
  final val ProtInstruction: UInt = "b100".U(ProtBits.W)

  // RRESP and BRESP encoding
  final val RespOkay:   UInt = "b00".U(RespBits.W)
  final val RespExOkay: UInt = "b01".U(RespBits.W)
  final val RespSlvRrr: UInt = "b10".U(RespBits.W)
  final val RespDecErr: UInt = "b11".U(RespBits.W)
}

abstract class AXI4Bundle extends CoreBundle with AXI4Config
abstract class AXI4Module extends CoreModule with AXI4Config with AXI4Spec
