package memory

import chisel3._
import core._

// AMBA AXI4-Lite bus protocol
// link: https://developer.arm.com/documentation/ihi0022/e/AMBA-AXI4-Lite-Interface-Specification
trait AXI4Spec extends CoreConfig {
  final val DataBits: Int = XLen

  final val BeatBytes: Int = 4 // An individual data transfer within an AXI burst

  // Protection encoding
  final val ProtBits:        Int = 3
  final val ProtPrivileged:  UInt = "b001".U(ProtBits.W)
  final val ProtInsecure:    UInt = "b010".U(ProtBits.W)
  final val ProtInstruction: UInt = "b100".U(ProtBits.W)

  // RRESP and BRESP encoding
  final val RespBits:   Int = 2
  final val RespOkay:   UInt = "0b00".U(RespBits.W)
  final val RespExOkay: UInt = "0b01".U(RespBits.W)
  final val RespSlvRrr: UInt = "0b10".U(RespBits.W)
  final val RespDecErr: UInt = "0b11".U(RespBits.W)
}

abstract class AXI4Bundle extends Bundle with AXI4Spec
