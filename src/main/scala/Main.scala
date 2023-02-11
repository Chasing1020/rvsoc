import chisel3.stage._
import core.fu.Alu
import firrtl.options.TargetDirAnnotation

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
