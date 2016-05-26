
package brain

import brain.compile.jvm.JVM
import brain.compile.javascript.JavaScript
import brain.ir.IR
import brain.Optimize
import brain.Parsers
import brain.Parsers.Success

package object util {
  def bfToJVM(bfCode: String, clsName: String, numCells: Int): Either[String, Array[Byte]] = {
    Parsers.parse(bfCode) match {
      case Success(ast, _) => {
        Right(JVM.irToJVM(Optimize.optimize(IR.astToIR(ast)), clsName,
                          numCells))
      }

      case err => {
        Left(err.toString)
      }
    }
  }


  def bfToJS(bfCode: String, numCells: Int): Either[String, String] = {
    Parsers.parse(bfCode) match {
      case Success(ast, _) => {
        Right(JavaScript.irToJS(Optimize.optimize(IR.astToIR(ast)), numCells))
      }

      case err => {
        Left(err.toString)
      }
    }
  }
}
