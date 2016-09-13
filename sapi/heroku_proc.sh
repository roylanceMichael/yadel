#!/usr/bin/env bash
if [ -d "/app/.jdk" ]; then
    export JAVA_HOME=/app/.jdk
fi

host="192.168.1.5"
nodeHost1=${host}
nodeHost2=${host}
nodePort1=2345
nodePort2=2346
export SeedCluster1="akka.ssl.tcp://YadelCluster@$nodeHost1:$nodePort1"
export SeedCluster2="akka.ssl.tcp://YadelCluster@$nodeHost2:$nodePort2"
export AkkaHost=$host
export PORT=8080

export JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom -Dakka.cluster.seed-nodes.0=$SeedCluster1 -Dakka.cluster.seed-nodes.1=$SeedCluster2"
sh target/bin/webapp