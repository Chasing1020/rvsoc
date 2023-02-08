package core

import chisel3._
import chisel3.util._

class CacheReq extends CfgBundle {
  val addr = UInt(XLen.W)
  val data = UInt(XLen.W)
  val mask = UInt((XLen / 8).W)
}

class CacheResp extends CfgBundle {
  val data = UInt(XLen.W)
}

class CacheIO extends CfgBundle {
  val abort = Input(Bool())
  val req = Flipped(Valid(new CacheReq))
  val resp = Valid(new CacheResp)
}

class Cache extends Module {
  val io = IO(new CacheIO)


}
