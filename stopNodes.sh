#!/bin/sh

for pid in `jps -m | grep receiver  | cut -d' ' -f1`
do
   echo "killing PID ${pid}"
   kill -9 ${pid}
done



