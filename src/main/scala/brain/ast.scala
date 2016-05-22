
package brain.ast


/*
 * A brainfuck AST consists simply of a list of operations.
 */

/**
 * One of the seven brainfuck operations. These include -, +, <, >, ., ,, and
 * the [] loop.
 */
sealed trait Operation
case class Loop(body: List[Operation]) extends Operation
case object Dec extends Operation
case object Inc extends Operation
case object ShiftLeft extends Operation
case object ShiftRight extends Operation
case object Read extends Operation
case object Write extends Operation
