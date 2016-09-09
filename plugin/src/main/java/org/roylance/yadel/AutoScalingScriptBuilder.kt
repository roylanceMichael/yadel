package org.roylance.yadel

import org.roylance.common.service.IBuilder
import java.io.File

class AutoScalingScriptBuilder(
        private val sshRSA: String,
        private val additionalSetup: String,
        private val projectName: String,
        private val fileLocation: String
): IBuilder<Boolean> {
    override fun build(): Boolean {
        val template = """#!/usr/bin/env bash
echo "ssh-rsa $sshRSA" >> /home/ubuntu/.ssh/authorized_keys

apt-get install --reinstall ca-certificates

apt-get update
$additionalSetup

tar -xvf ${CommonStringsHelper.ServerTarName}
bash ${CommonStringsHelper.CreateAndInstallPackageName}
pushd /opt/$projectName
at now + 1 minutes -f /opt/$projectName/${CommonStringsHelper.buildStartActorName(projectName)}
"""
        File(fileLocation).delete()
        File(fileLocation).writeText(template)
        return true
    }
}