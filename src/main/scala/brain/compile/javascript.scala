
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
    val init = ("arr = [];\n"
                + ("for(var i = 0; i < " + numCells.toString
                   + "; i++) {arr[i] = 0;}\n")
                + "idx = 0;\n"
                + "input = \"\";\n")

    val read = ("function read() {"
                + "while(input.length == 0) {input = prompt(\"Enter text\")}"
                + "arr[idx] = input.charCodeAt(0);"
                + "input = input.substring(1);"
                + "}\n")

    val write = ("function write() {"
                 + "document.write(String.fromCharCode(arr[idx]));"
                 + "}\n")

    val main = ir.map(convertIR).fold("")(_+_)

    init + read + write + main
  }

  def convertIR(ir: IR): String = ir match {
    case ModPointer(amount) => "idx += " + amount.toString + ";\n"

    case ModValue(amount) => "arr[idx] += " + amount.toString + ";\n"

    case Read(times) => List.fill(times)("read();\n").fold("")(_+_)

    case Write(times) => List.fill(times)("write();\n").fold("")(_+_)

    case Loop(id, body) => ("while(arr[idx] != 0) {\n"
                            + body.map(convertIR).fold("")(_+_)
                            + "}\n")
  }
}
