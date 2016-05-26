
Javascript
==========

start
-----

| arr = [];
|
| for(var i = 0; i < 30000; i++) {arr[i] = 0;}
|
| idx = 0;
| input = "";
|
| function read() {
|     while(input.length == 0) {input = prompt("Enter text")}
|     arr[idx] = input.charCodeAt(0);
|     input = input.substring(1);
| }
|
| function write() {
|     document.write(String.fromCharCode(arr[idx]));
| }

Conversions
-----------

| ModPointer(#)
|     idx += #;
|
| ModValue(#)
|     arr[idx] += #;
|
| Read(#) - Repeat # times.
|     read();
|
| Write(#) - Repeat # times.
|     write();
|
| Loop(#, ...)
|     while(arr[idx] != 0) {
|         ...
|     }

end
---
