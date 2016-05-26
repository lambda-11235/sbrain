
package brain.compile.javascript

import brain.ir._

object JavaScript {

  /**
   * Converts IR to javascript.
   *
   * @param ir The IR to convert to bytecode.
   * @param numCells The number of cells supported at runtime.
   */
  def irToJS(ir: List[IR], numCells: Int): String = {
    val init = ("arr = Array.apply(null, Array(" + numCells.toString
                + ")).map(Number.prototype.valueOf, 0)\n"
                + "idx = 0\n\n")

    val main = ir.map(convertIR).fold("")(_+_)

    init + main
  }

  def convertIR(ir: IR): String = ir match {
    case ModPointer(amount) => "idx += " + amount.toString + "\n"

    case ModValue(amount) => "arr[idx] += " + amount.toString + "\n"

    case Read(times) => List.fill(times)("arr[idx] = prompt(\"Enter character\")"
                                         + ".charCodeAt(0)\n").fold("")(_+_)

    case Write(times) => List.fill(times)("document.write(String.fromCharCode"
                                          + "(arr[idx]))").fold("")(_+_)

    case Loop(id, body) => ("while(arr[idx] != 0) {\n"
                            + body.map(convertIR).fold("")(_+_)
                            + "}\n")
  }
}