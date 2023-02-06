import chisel3.stage._
import firrtl.options.TargetDirAnnotation
import core.Adder

object Main extends App {
  def defaultDir = "./build"

  new ChiselStage().execute(
    args,
    Seq(
      ThrowOnFirstErrorAnnotation,
      ChiselGeneratorAnnotation(() => new Adder(4)),
      TargetDirAnnotation(if (args.isEmpty) defaultDir else args.head)
    )
  )
}
