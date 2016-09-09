package org.roylance.yadel

object CommonStringsHelper {
    const val DebianBuild = "build/debian/"
    const val DebianBuildBuild = "build/debian/build/"
    const val BuildInstallPath = "build/install/"
    const val SBinPath = "${DebianBuildBuild}usr/sbin/"

    const val AWSDesiredCapacityName = "aws_desired_capacity.sh"
    const val CreateAndInstallPackageName = "create_install_package.sh"
    const val AutoScalingName = "auto_scaling.sh"
    const val ServerTarName = "server.tar"
    const val StartupName = "startup.sh"

    fun buildProjectStartServerScriptLocation(projectName: String): String {
        return buildInstallPath(projectName, buildStartServerName(projectName))
    }

    fun buildProjectStartActorScriptLocation(projectName: String): String {
        return buildInstallPath(projectName, buildStartActorName(projectName))
    }

    fun buildSystemStartServerScriptLocation(projectName: String): String {
        return "$SBinPath${buildStartServerName(projectName)}"
    }

    fun buildSystemStartActorScriptLocation(projectName: String): String {
        return "$SBinPath${buildStartActorName(projectName)}"
    }

    fun buildSystemStopScriptLocation(projectName: String): String {
        return "$SBinPath${buildStopName(projectName)}"
    }

    fun buildProjectStopScriptLocation(projectName: String): String {
        return buildInstallPath(projectName, buildStopName(projectName))
    }

    fun buildInstallPath(projectName: String, fileName: String): String {
        return "$BuildInstallPath$projectName/$fileName"
    }

    fun buildStartActorName(projectName: String): String {
        return "${projectName}_actor.sh"
    }

    fun buildStartServerName(projectName: String): String {
        return "${projectName}_server.sh"
    }

    fun buildStopName(projectName: String): String {
        return "${projectName}_stop.sh"
    }
}