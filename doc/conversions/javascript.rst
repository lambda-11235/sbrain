
Javascript
==========

start
-----

| arr = Array.apply(null, Array(30000)).map(Number.prototype.valueOf, 0);
| idx = 0
| input = ""


Conversions
-----------

| ModPointer(#)
|     idx += #
|
| ModValue(#)
|     arr[idx] += #
|
| Read(#) - Repeat # times.
|     if(input.length == 0) {input = prompt("Enter text")}
|     arr[idx] = input.charAt(0)
|     input = input.substring(1)
|
| Write(#) - Repeat # times.
|     document.write(String.fromCharCode(arr[idx]))
|
| Loop(#, ...)
|     while(arr[idx] != 0) {
|         ...
|     }

end
---
