package org.roylance.yadel

import org.roylance.common.service.IBuilder
import java.io.File

class CreateInstallPackageBuilder(
        private val version: String,
        private val projectName: String,
        private val fileLocation: String
): IBuilder<Boolean> {
    override fun build(): Boolean {
        val template = """#!/usr/bin/env bash
apt-get install --reinstall ca-certificates
add-apt-repository -y ppa:webupd8team/java

echo debconf shared/accepted-oracle-license-v1-1 select true | \
  debconf-set-selections
echo debconf shared/accepted-oracle-license-v1-1 seen true | \
  debconf-set-selections

apt-get install -y oracle-java8-installer

service $projectName stop
apt-get -y remove $projectName
rm -rf /opt/$projectName

dpkg -i ${projectName}_${version}_all.deb
apt-get update
apt-get install -f -y
dpkg -i ${projectName}_${version}_all.deb
"""
        File(fileLocation).delete()
        File(fileLocation).writeText(template)

        return true
    }

}