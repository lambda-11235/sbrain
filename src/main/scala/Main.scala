
import brain.compile.jvm.JVM
import brain.ir.IR
import brain.Optimize
import brain.Parsers
import brain.Parsers.Success

import java.io.FileOutputStream


object Main {
  def main(args: Array[String]) {
    if(args.length != 4) {
      println("Usage: exec infile outfile classname numcells")
      System.exit(1)
    }

    val infile = scala.io.Source.fromFile(args(0))
    val outfile = new FileOutputStream(args(1));
    //val outfile = scalax.io.Resource.fromFile(args(1))
    val clsName = args(2)
    val numCells = args(3).toInt

    Parsers.parse(infile.mkString) match {
      case Success(ast, _) => {
        outfile.write(JVM.irToJVM(Optimize.optimize(IR.astToIR(ast)),
                                                    clsName, numCells))

        infile.close()
        outfile.close()
      }

      case err => {
        infile.close()
        outfile.close()

        println(err)
        System.exit(1)
      }
    }
  }
}
