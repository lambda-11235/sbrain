
package brain

import brain.ir._


object Optimize {

  /** Applies all optimizations to IR code. */
  def optimize(ops: List[IR]): List[IR] = joinDups(removeLeadingLoops(ops))


  /**
   * Removes all loops that occur before any other command from the program.
   * These loops will never be executed since all cells are set to zero.
   */
  def removeLeadingLoops(ops: List[IR]): List[IR] = ops match {
    case Loop(_, _) :: moreOps => removeLeadingLoops(moreOps)
    case moreOps => moreOps
  }


  /**
   * Joins consecutive duplicate operations. For example, ModPointer(2) followed
   * by ModPointer(-1) is optimized to ModPointer(1).
   */
  def joinDups(ops: List[IR]): List[IR] = ops match {
    case ModPointer(a) :: ModPointer(b) :: moreOps => {
      joinDups(ModPointer(a + b) :: moreOps)
    }

    case ModValue(a) :: ModValue(b) :: moreOps => {
      joinDups(ModValue(a + b) :: moreOps)
    }

    case Read(a) :: Read(b) :: moreOps => {
      joinDups(Read(a + b) :: moreOps)
    }

    case Write(a) :: Write(b) :: moreOps => {
      joinDups(Write(a + b) :: moreOps)
    }

    case Loop(id, body) :: moreOps => {
      Loop(id, joinDups(body)) :: joinDups(moreOps)
    }

    case op :: moreOps => op :: joinDups(moreOps)

    case Nil => Nil
  }
}
