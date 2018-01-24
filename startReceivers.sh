#!/bin/bash

for I in {1..5}
do
  echo "Starting receiver$I "
  mvn exec:java -Dexec.args=AkkaRouter.ReceiverApp -Dname=receiver$I -Dakka.remote.netty.tcp.port=500$I  &
done

