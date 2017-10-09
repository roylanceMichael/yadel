package org.roylance.yadel

import org.roylance.common.service.IBuilder
import java.io.File

class DebianPackageBuilder(
        private val projectName: String,
        private val version: String,
        private val maintainerInfo: String,
        private val fileLocation: String
): IBuilder<Boolean> {
    override fun build(): Boolean {
        val template = """Package: $projectName
Version: $version
Section: base
Priority: optional
Architecture: all
Depends: oracle-java8-installer
Maintainer: $maintainerInfo
Description: akka cluster system for $projectName
"""
        File(fileLocation).delete()
        File(fileLocation).writeText(template)
        return true
    }
}