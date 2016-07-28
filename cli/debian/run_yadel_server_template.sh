#!/usr/bin/env bash
nodeHost1=${host}
nodeHost2=${host}
nodePort1=2345
nodePort2=2346
export SeedCluster1="akka.ssl.tcp://YadelCluster@$nodeHost1:$nodePort1"
export SeedCluster2="akka.ssl.tcp://YadelCluster@$nodeHost2:$nodePort2"

export JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom -Dakka.cluster.seed-nodes.0=$SeedCluster1 -Dakka.cluster.seed-nodes.1=$SeedCluster2"
# seed node 1
bin/cli ${host} ${nodePort1} > seedNode1.out 2>&1&
# seed node 2
bin/cli ${host} ${nodePort2} > seedNode2.out 2>&1&
# manager
export JAVA_OPTS="${JAVA_OPTS} -Xmx2g -Xms1g"
echo ${JAVA_OPTS}
bin/cli ${host} 2344 > manager.out 2>&1&