
package brain.lexer

import scala.util.parsing.input.{Position, Reader}


sealed trait Token
case object Plus extends Token
case object Minus extends Token
case object LessThan extends Token
case object GreaterThan extends Token
case object Comma extends Token
case object Period extends Token
case object LeftBracket extends Token
case object RightBracket extends Token


object Lexer {

  /**
   * The position of a token.
   *
   * @param column The column the token starts at.
   * @param line The line the token is on.
   * @param lineContents The source code of the line containing the token.
   */
  private case class LexPos(column: Int, line: Int, lineContents: String) extends Position

  /**
   * The lexical output, which is a token and its position.
   */
  private case class LexOut(token: Token, pos: LexPos)

  /**
   * A token reader for use with the parser.
   *
   * @param lexout The output of the lexer.
   * @param LesPos The position of the last token read.
   */
  private case class TokenReader(lexout: List[LexOut], lastPos: LexPos =
    LexPos(0, 0, "")) extends Reader[Token] {
      def atEnd = lexout.isEmpty
      def first = lexout.head.token
      def pos = if(lexout.isEmpty) lastPos else lexout.head.pos
      def rest = TokenReader(lexout.tail, lexout.head.pos)
    }

  /**
   * Converts a string into a token reader for parsing.
   */
  def lex(str: String): Reader[Token] = {
    val lines = str.split('\n').flatMap(_.split('\r'))

    var column = 1
    var line = 1
    var xs = List.empty[LexOut]

    def getLC: String = str
    def add(tok: Token) = { xs = LexOut(tok, LexPos(column, line,
                                                  lines(line - 1))) :: xs }

    for(ch <- str) {
      ch match {
        case '+' => add(Plus)
        case '-' => add(Minus)
        case '<' => add(LessThan)
        case '>' => add(GreaterThan)
        case ',' => add(Comma)
        case '.' => add(Period)
        case '[' => add(LeftBracket)
        case ']' => add(RightBracket)
        case _ => ()
      }

      if(ch == '\n' || ch == '\r') {
        column = 1
        line += 1
      } else {
        column += 1
      }
    }

    TokenReader(xs.reverse)
  }
}
