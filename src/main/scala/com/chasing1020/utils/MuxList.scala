package com.chasing1020.utils

import chisel3._

object MuxList {
  def apply[T <: Data](addr: UInt, default: List[T], mapping: Array[(UInt, List[T])]): List[T] = {
    val map = mapping.map(m => (m._1 === addr, m._2))
    default.zipWithIndex.map { case (d, i) => map.foldRight(d)((m, n) => Mux(m._1, m._2(i), n)) }
  }
}
