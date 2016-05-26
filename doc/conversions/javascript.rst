
Javascript
==========

start
-----

| arr = Array.apply(null, Array(30000)).map(Number.prototype.valueOf, 0);
| idx = 0


Conversions
-----------

| ModPointer(#)
|     idx += #
|
| ModValue(#)
|     arr[idx] += #
|
| Read(#) - Repeat # times.
|     arr[idx] = prompt("Enter character").charCodeAt(0)
|
| Write(#) - Repeat # times.
|     document.write(String.fromCharCode(arr[idx]))
|
|
| Loop(#, ...)
|     while(arr[idx] != 0) {
|         ...
|     }

end
---
