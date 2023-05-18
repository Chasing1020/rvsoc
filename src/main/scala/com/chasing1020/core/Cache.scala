package com.chasing1020.core

import chisel3._
import chisel3.util._
import chisel3.experimental._
import com.chasing1020.utils.EmitVerilog

class CacheReq extends CacheBundle {
  val addr = UInt(XLen.W)
  val data = UInt(XLen.W)
  val mask = UInt((XLen / 8).W)
}

class CacheResp extends CacheBundle {
  val data = UInt(XLen.W)
}

class CacheIO extends CacheBundle {
  val abort = Input(Bool())
  val req = Flipped(Valid(new CacheReq))
  val resp = Valid(new CacheResp)
}

object CacheState extends ChiselEnum {}

class Cache extends CacheModule {
  val io = IO(new CacheIO)
  val idle :: compareTag :: allocate :: writeBack :: Nil = Enum(4)

  // todo: add cache
}

//object Cache {
//  def main(args: Array[String]): Unit = {
//    EmitVerilog(new Cache)
//  }
//}