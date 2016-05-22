
C Conversion
============

Here's a quick sketch of how IR code should compile to C.
Note that the # is a number.

Start
-----

| #include <stdio.h>
|
| int main() {
|     char cells[30000] = {0};
|     char* ptr = cells;

Conversions
-----------

| ModPointer(#)
|     ptr += #;
|
| ModValue(#)
|     * ptr += #;
|
| Read(#) - Repeat # times.
|     scanf("%c", ptr);
|
| Write(#) - Repeat # times.
|     printf("%c", * ptr);
|
| Loop(#, ...)
|     while(* ptr) {
|         ...
|     }

End
---

|     return 0;
| }
