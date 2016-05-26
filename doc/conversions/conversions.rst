
====================
Compiler Conversions
====================

The following sections document the representations and conversion performed by
the compiler.

.. contents::

Intermediate Representation
===========================

The intermediate representation consists of 4 forms. Let # be some number.

| ModPointer(#) - Modifies the cell pointer by adding # to it.
| ModValue(#)   - Modifies the current cell value by adding # to it.
| Read(#)       - Reads a character value into the current cell # times.
| Write(#)      - Writes the character value of the current cell # times.
| Loop(#, ...)  - Loops over its body in the same fashion as BF. # is a unique ID number.

.. include:: jvm.rst
.. include:: mips.rst
.. include:: c.rst
.. include:: javascript.rst
