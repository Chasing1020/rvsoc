import chisel3.stage._
import core.exu.fu.Alu
import firrtl.options.TargetDirAnnotation
import utils._

object Main extends App {
  def defaultDir = "./build"
  Logger.level = Level.Never

  new ChiselStage().execute(
    args,
    Seq(
      ThrowOnFirstErrorAnnotation,
      ChiselGeneratorAnnotation(() => new Alu(32)),
      TargetDirAnnotation(if (args.isEmpty) defaultDir else args.head)
    )
  )
}
