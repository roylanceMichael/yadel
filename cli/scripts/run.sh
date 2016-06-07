#!/usr/bin/env bash
#!/usr/bin/env bash
host=$1
port=$2
nodeHost1=$3
nodePort1=$4
nodeHost2=$5
nodePort2=$6

if [ -z "$1" ]; then
	host="127.0.0.1"
fi
if [ -z "$2" ]; then
	port="0"
fi
if [ -z "$3" ]; then
	nodeHost1="127.0.0.1"
fi
if [ -z "$4" ]; then
	nodePort1="2345"
fi
if [ -z "$5" ]; then
	nodeHost2="127.0.0.1"
fi
if [ -z "$6" ]; then
	nodePort2="2346"
fi

export SeedCluster1="akka.tcp://YadelCluster@$nodeHost1:$nodePort1"
export SeedCluster2="akka.tcp://YadelCluster@$nodeHost2:$nodePort2"

export JAVA_OPTS='-Dakka.cluster.seed-nodes.0=$SeedCluster1 -Dakka.cluster.seed-nodes.1=$SeedCluster2'
bin/yadel.cli ${host} ${port}