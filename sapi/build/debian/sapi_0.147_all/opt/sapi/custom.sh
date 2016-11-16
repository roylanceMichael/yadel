#!/usr/bin/env bash
#// This file was auto-generated, but can be altered. It will not be overwritten.
host="192.168.12.130"
#host="192.168.1.5"
nodeHost1=${host}
nodeHost2=${host}
nodePort1=2345
nodePort2=2346
export SeedCluster1="akka.ssl.tcp://YadelCluster@$nodeHost1:$nodePort1"
export SeedCluster2="akka.ssl.tcp://YadelCluster@$nodeHost2:$nodePort2"
export AkkaHost=$host
export PORT=8081
