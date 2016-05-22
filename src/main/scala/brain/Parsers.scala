
package brain

import brain.ast._
import brain.lexer._

import scala.util.parsing.combinator.Parsers


package object Parsers extends Parsers {
  type Elem = Token

  def parse(str: String): ParseResult[List[Operation]] = phrase(ops)(Lexer.lex(str))

  def ops: Parser[List[Operation]] = op.*
  def op: Parser[Operation] = dec | inc | shiftleft | shiftright | read | write | loop
  def dec: Parser[Operation] = accept(Minus) ^^^ Dec
  def inc: Parser[Operation] = accept(Plus) ^^^ Inc
  def shiftleft: Parser[Operation] = accept(LessThan) ^^^ ShiftLeft
  def shiftright: Parser[Operation] = accept(GreaterThan) ^^^ ShiftRight
  def read: Parser[Operation] = accept(Comma) ^^^ Read
  def write: Parser[Operation] = accept(Period) ^^^ Write
  def loop: Parser[Operation] = accept(LeftBracket) ~ ops ~ accept(RightBracket) ^^ {
    case _ ~ xs ~ _ => Loop(xs)
  }
}
