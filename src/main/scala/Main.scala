import chisel3.stage._
import firrtl.options.TargetDirAnnotation
import core.Alu

object Main extends App {
  def defaultDir = "./build"

  new ChiselStage().execute(
    args,
    Seq(
      ThrowOnFirstErrorAnnotation,
      ChiselGeneratorAnnotation(() => new Alu(32)),
      TargetDirAnnotation(if (args.isEmpty) defaultDir else args.head)
    )
  )
}
