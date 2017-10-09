package org.roylance.yadel

import org.roylance.common.service.IBuilder
import java.io.File

class RunServerBuilder(private val projectName: String,
                       private val host: String,
                       private val fileLocation: String,
                       private val managerPort: Int = 2234,
                       private val seedIPPort1: Int = 2345,
                       private val seedIPPort2: Int = 2346,
                       private val isSystemWide: Boolean = false
): IBuilder<Boolean> {
    override fun build(): Boolean {
        val template = """#!/usr/bin/env bash
${if (isSystemWide) "pushd /opt/$projectName" else ""}
export JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom -Dakka.cluster.seed-nodes.0=akka.ssl.tcp://YadelCluster@$host:$seedIPPort1 -Dakka.cluster.seed-nodes.1=akka.ssl.tcp://YadelCluster@$host:$seedIPPort2"
# seed node 1
rm -rf seed1
mkdir seed1
pushd seed1
cp ../*.keystore .
cp ../*.truststore .
../bin/$projectName $host $seedIPPort1 > seedNode1.out 2>&1&
popd
# seed node 2
rm -rf seed2
mkdir seed2
pushd seed2
cp ../*.keystore .
cp ../*.truststore .
../bin/$projectName $host $seedIPPort2 > seedNode2.out 2>&1&
popd
# manager
bin/$projectName $host $managerPort > manager.out 2>&1&
"""
        File(fileLocation).delete()
        File(fileLocation).writeText(template)
        return true
    }
}