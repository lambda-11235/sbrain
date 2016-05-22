
MIPS Conversion
===============

Here's a quick sketch of how IR code should compile to MIPS.
Note that the # is a number.

Start
-----

|     .data
| cells: .space 30000
|
|     .text
|     .globl main
|
| main:
|     la $t0, cells

Conversions
-----------

| ModPointer(#)
|     add $t0, $t0, #
|
| ModValue(#)
|     lb $t1, 0($t0)
|     add $t1, $t1, #
|     sb $t1, 0($t0)
|
| Read(#) - Repeat # times.
|     li $v0, 12
|     syscall
|     sb $v0, 0($t0)
|
| Write(#) - Repeat **only** syscall # times.
|     li $v0, 11
|     lb $a0, 0($t0)
|     syscall
|
| Loop(#, ...)
|     loop#:
|         lb $t1, 0($t0)
|         beqz $t1, end#
|
|         ...
|
|         j loop#
|     end#:

End
---

|     li $v0, 10
|     syscall
