
package brain.ir


/**
 * The intermediate representation for compilation. It is used to annotate
 * loops and do other optimizations before converting to the target language.
 */
sealed trait IR

/** A change in the tape pointer. */
case class ModPointer(amount: Int) extends IR

/** A change in the value currently pointed to on the tape. */
case class ModValue(amount: Int) extends IR

/** Read the value onto the tape a certain number of times. */
case class Read(times: Int) extends IR

/** Write a value from the tape to stdout a certain number of times. */
case class Write(times: Int) extends IR

/** Loop over a some code.
 *
 * @param id A unique ID used in compilation.
 * @param body A list of the operations in the loop.
 */
case class Loop(id: Int, body: List[IR]) extends IR



object IR {
  /** Convert the brainfuck AST to IR. */
  def astToIR(ops: List[brain.ast.Operation]): List[IR] =
    labelLoops(convertOps(ops))

  private def convertOps(ops: List[brain.ast.Operation]): List[IR] = {
    ops.map {
      case brain.ast.Dec => ModValue(-1)
      case brain.ast.Inc => ModValue(1)
      case brain.ast.ShiftLeft => ModPointer(-1)
      case brain.ast.ShiftRight => ModPointer(1)
      case brain.ast.Read => Read(1)
      case brain.ast.Write => Write(1)
      case brain.ast.Loop(body) => Loop(0, convertOps(body))
    }
  }

  private def labelLoops(ops: List[IR]): List[IR] = {
    var uniqID = 0

    def subLabel(sops: List[IR]): List[IR] = {
      sops.map {
        case Loop(_, body) => {
          val id = uniqID
          uniqID += 1
          val newBody = subLabel(body)
          Loop(id, newBody)
        }

        case op => op
      }
    }

    subLabel(ops)
  }
}
