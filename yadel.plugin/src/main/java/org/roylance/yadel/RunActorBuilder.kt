package org.roylance.yadel

import org.roylance.common.service.IBuilder
import java.io.File

class RunActorBuilder(
        private val projectName: String,
        private val host: String,
        private val fileLocation: String,
        private val actorPort: Int = 2234,
        private val seedIPPort1: Int = 2345,
        private val seedIPPort2: Int = 2346,
        private val isSystemWide: Boolean = false
): IBuilder<Boolean> {
    override fun build(): Boolean {
        val template = """#!/usr/bin/env bash
${if (isSystemWide) "pushd /opt/$projectName" else ""}
export JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom -Dakka.cluster.seed-nodes.0=akka.ssl.tcp://YadelCluster@$host:$seedIPPort1 -Dakka.cluster.seed-nodes.1=akka.ssl.tcp://YadelCluster@$host:$seedIPPort2"
rm -rf current_$actorPort
mkdir current_$actorPort
pushd current_$actorPort
cp ../*.keystore .
cp ../*.truststore .
../bin/$projectName $host $actorPort > current_$actorPort.out 2>&1&
"""
        File(fileLocation).delete()
        File(fileLocation).writeText(template)

        return true
    }
}