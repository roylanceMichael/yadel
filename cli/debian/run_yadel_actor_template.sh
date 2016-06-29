#!/usr/bin/env bash
nodeHost1=${host}
nodePort1=2345
nodeHost2=${host}
nodePort2=2346
token=$1
if [ -z "$1" ]; then
	token="current"
fi

export SeedCluster1="akka.ssl.tcp://YadelCluster@$nodeHost1:$nodePort1"
export SeedCluster2="akka.ssl.tcp://YadelCluster@$nodeHost2:$nodePort2"

export JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom -Dakka.cluster.seed-nodes.0=$SeedCluster1 -Dakka.cluster.seed-nodes.1=$SeedCluster2"
bin/yadel.cli ${host} > ${token}_1_worker.out 2>&1&
bin/yadel.cli ${host} > ${token}_2_worker.out 2>&1&
bin/yadel.cli ${host} > ${token}_3_worker.out 2>&1&
bin/yadel.cli ${host} > ${token}_4_worker.out 2>&1&