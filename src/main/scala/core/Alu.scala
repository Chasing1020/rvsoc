package core

import chisel3._
import chisel3.util._

case object AluOp {
  final val Unknown: UInt = "b0000".U
  final val Add:     UInt = "b0001".U
  final val Sub:     UInt = "b0010".U
  final val And:     UInt = "b0011".U
  final val Or:      UInt = "b0100".U
  final val Xor:     UInt = "b0101".U
  final val Slt:     UInt = "b0110".U
  final val Sll:     UInt = "b0111".U
  final val Sltu:    UInt = "b1000".U
  final val Srl:     UInt = "b1001".U
  final val Sra:     UInt = "b1010".U
  final val Lui:     UInt = "b1011".U
}

class AluIO(width: Int) extends Bundle {
  val a = Input(UInt(width.W))
  val b = Input(UInt(width.W))
  val op = Input(UInt(4.W))
  val out = Output(UInt(width.W))
}

class Alu(width: Int) extends Module {
  val io = IO(new AluIO(width))

  val shamt = io.b(4, 0)
  val opList = List(
    AluOp.Add -> (io.a + io.b),
    AluOp.Sub -> (io.a - io.b),
    AluOp.And -> (io.a & io.b),
    AluOp.Or -> (io.a | io.b),
    AluOp.Xor -> (io.a ^ io.b),
    AluOp.Slt -> (io.a.asSInt < io.b.asSInt).asUInt,
    AluOp.Sll -> (io.a << shamt),
    AluOp.Sltu -> (io.a < io.b).asUInt,
    AluOp.Srl -> (io.a >> shamt),
    AluOp.Sra -> (io.a.asSInt >> shamt).asUInt,
    AluOp.Lui -> io.b
  )
  io.out := MuxLookup(key = io.op, default = 0.U, mapping = opList)

//  printf(s"a: %d, b: %d, op: %d, out: %d\n", io.a, io.b, io.op, io.out)
}

object Alu {
  def apply(width: Int, a: UInt, b: UInt, op: UInt) = {
    val alu = new Alu(width)
    alu.io.a := a
    alu.io.b := b
    alu.io.op := op
    alu.io.out
  }
}
