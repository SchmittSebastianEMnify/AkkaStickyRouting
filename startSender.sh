#!/bin/bash

  echo "Starting sender"
  mvn exec:java -Dexec.args=AkkaRouter.SenderApp -Dname=sender$I


