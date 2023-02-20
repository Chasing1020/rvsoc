package core.wbu

import chisel3._
import chisel3.stage.ChiselStage
import chisel3.util.Counter
import chiseltest._
import core.idu.Idu
import core.RegFile
import core.exu.Exu
import firrtl.options.TargetDirAnnotation
import org.scalatest.flatspec.AnyFlatSpec
import utils._

class WbuTester extends TraceTester {
  val idu = Module(new Idu)
  val exu = Module(new Exu)
  val wbu = Module(new Wbu)
  val rf = Module(new RegFile)

  idu.io.in.pc := 4.U
  rf.io.r1 <> idu.io.rfr1
  rf.io.r2 <> idu.io.rfr2
  exu.io.in <> idu.io.out
  wbu.io.in <> exu.io.out
  rf.io.w.data := wbu.io.in.rfw.data
  rf.io.w.en := wbu.io.in.rfw.en
  rf.io.w.addr := wbu.io.in.rfw.addr
//  rf.io.w <> wbu.io.rf

  val insts = VecInit(
    Seq(
      "x00a28313".U, // addi x6, x5, 10
      "x00a30393".U // addi x7, x6, 10
    )
  )
  val (i, done) = Counter(true.B, insts.size)

  idu.io.in.inst := insts(i)

  when(done) { stop() }

  Info(cf"Case: $i, Inst: ${insts(i)}")
  Debug(cf"[idu.io.out.data]: ${idu.io.out.data}")
  Debug(cf"[idu.io.out.fc]: ${idu.io.out.fc}")
  Debug(cf"[wbu.io.in.rfw]: ${wbu.io.in.rfw}")
  Debug(cf"[Wbu.io.in.br]: ${wbu.io.in.br}")
  Debug(cf"[wbu.io.out]: ${wbu.io.out}")
}

class WbuTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior.of("Wbu")

  it should "success" in {
    test(new WbuTester).withAnnotations(Seq(WriteVcdAnnotation)).runUntilStop()

    (new ChiselStage).emitVerilog(
      gen = new Wbu,
      annotations = Seq(TargetDirAnnotation("test_run_dir/Wbu_should_success/"))
    )
  }
}
