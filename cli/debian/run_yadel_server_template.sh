#!/usr/bin/env bash
nodeHost1=${host}
nodeHost2=${host}
nodePort1=2345
nodePort2=2346
export SeedCluster1="akka.ssl.tcp://YadelCluster@$nodeHost1:$nodePort1"
export SeedCluster2="akka.ssl.tcp://YadelCluster@$nodeHost2:$nodePort2"

export JAVA_OPTS='-Dakka.cluster.seed-nodes.0=$SeedCluster1 -Dakka.cluster.seed-nodes.1=$SeedCluster2'
# seed node 1
bin/yadel.cli ${host} ${nodePort1} > seedNode1.out 2>&1&
# seed node 2
bin/yadel.cli ${host} ${nodePort2} > seedNode2.out 2>&1&
# manager
bin/yadel.cli ${host} 2344 > manager.out 2>&1&