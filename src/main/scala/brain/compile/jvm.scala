
package brain.compile.jvm

import brain.ir._

import org.objectweb.asm.{ClassWriter, Label, MethodVisitor, Opcodes}

object JVM {

  /**
   * Converts IR to JVM bytecode.
   *
   * @param ir The IR to convert to bytecode.
   * @param clsName The name of the output class.
   * @param numCells The number of cells supported at runtime.
   */
  def irToJVM(ir: List[IR], clsName: String, numCells: Int): Array[Byte] = {
    val cw = new ClassWriter(0)

    startClass(cw, clsName)

    val mw = cw.visitMethod(Opcodes.ACC_STATIC + Opcodes.ACC_PUBLIC, "main",
                            "([Ljava/lang/String;)V", null, null)

    startMain(mw, numCells)
    convertOps(ir, mw)
    endMain(mw)

    cw.visitEnd()

    cw.toByteArray()
  }

  private def startClass(cw: ClassWriter, clsName: String): Unit = {
    val qualifiedName = clsName.replace('.', '/')

    cw.visit(Opcodes.V1_5, Opcodes.ACC_PUBLIC, qualifiedName, null,
             "java/lang/Object", null)

    val mw = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null)
    mw.visitVarInsn(Opcodes.ALOAD, 0)
    mw.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>",
                       "()V", false)
    mw.visitInsn(Opcodes.RETURN)
    mw.visitMaxs(1, 1)
    mw.visitEnd()
  }

  private def startMain(mw: MethodVisitor, numCells: Int): Unit = {
    mw.visitIntInsn(Opcodes.SIPUSH, numCells)
    mw.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_CHAR)
    mw.visitVarInsn(Opcodes.ASTORE, 1)
    mw.visitInsn(Opcodes.ICONST_0)
    mw.visitIntInsn(Opcodes.ISTORE, 2)
  }

  private def endMain(mw: MethodVisitor): Unit = {
    mw.visitInsn(Opcodes.RETURN)
    mw.visitMaxs(5, 5)
    mw.visitEnd()
  }

  private def convertOps(ops: List[IR], mw: MethodVisitor): Unit =
    ops.foreach(op => convertOp(op, mw))


  private def convertOp(op: IR, mw: MethodVisitor): Unit = op match {
    case ModPointer(amount) => mw.visitIincInsn(2, amount)

    case ModValue(amount) => {
      mw.visitVarInsn(Opcodes.ALOAD, 1)
      mw.visitVarInsn(Opcodes.ILOAD, 2)
      mw.visitInsn(Opcodes.DUP2)
      mw.visitInsn(Opcodes.CALOAD)
      mw.visitIntInsn(Opcodes.SIPUSH, amount)
      mw.visitInsn(Opcodes.IADD)
      mw.visitInsn(Opcodes.I2C)
      mw.visitInsn(Opcodes.CASTORE)
    }

    case Read(times) => {
      for(_ <- (1 to times)) {
        mw.visitVarInsn(Opcodes.ALOAD, 1)
        mw.visitVarInsn(Opcodes.ILOAD, 2)
        mw.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "in",
                          "Ljava/io/InputStream;")
        mw.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/InputStream",
                           "read", "()I", false)
        mw.visitInsn(Opcodes.I2C)
        mw.visitInsn(Opcodes.CASTORE)
      }
    }

    case Write(times) => {
      for(_ <- (1 to times)) {
        mw.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out",
                          "Ljava/io/PrintStream;")
        mw.visitVarInsn(Opcodes.ALOAD, 1)
        mw.visitVarInsn(Opcodes.ILOAD, 2)
        mw.visitInsn(Opcodes.CALOAD)
        mw.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream",
                           "print", "(C)V", false)
      }
    }

    case Loop(_, body) => {
      val begin = new Label()
      val end = new Label()

      mw.visitLabel(begin)
      mw.visitVarInsn(Opcodes.ALOAD, 1)
      mw.visitVarInsn(Opcodes.ILOAD, 2)
      mw.visitInsn(Opcodes.CALOAD)
      mw.visitJumpInsn(Opcodes.IFEQ, end)

      convertOps(body, mw)

      mw.visitJumpInsn(Opcodes.GOTO, begin)
      mw.visitLabel(end)
    }
  }
}
