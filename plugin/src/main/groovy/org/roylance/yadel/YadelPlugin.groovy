package org.roylance.yadel

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.nio.file.Files


class YadelPlugin extends DefaultTask {
    public String projectName = ""
    public String serverUrl = ""
    public String serverVersion = ""

    public String sshRSA = ""
    public String additionalAutoScalingSetup = ""

    public String awsAutoScalingGroupName = ""

    public String keystoreName = ""
    public String truststoreName = ""

    public String fullServerTarUrl = ""
    public String maintainerInfo = ""

    public Integer actorPort = 2234
    public Integer seedNode1Port = 2345
    public Integer seedNode2Port = 2346
    public Integer managerPort = 2344

    @TaskAction
    def packageServer() {
        println("building hades with server: " + serverUrl + " and version: " + serverVersion)

        if (keystoreName.length() == 0) {
            println("unknown keystore location")
            return
        }
        if (truststoreName.length() == 0) {
            println("unknown trustore location")
            return
        }

        def keystoreFile = new File("src/main/resources/$keystoreName")
        if (!keystoreFile.exists()) {
            println("keystore file does not exist")
        }

        def truststoreFile = new File("src/main/resources/$truststoreName")
        if (!truststoreFile.exists()) {
            println("truststore file does not exist")
            return
        }

        if (projectName.length() == 0) {
            println("unknown projectName")
            return
        }

        if (fullServerTarUrl.length() == 0) {
            println("unknown fullServerTarUrl")
            return
        }

        if (maintainerInfo.length() == 0) {
            println("unknown maintainerInfo")
            return
        }

        if (serverUrl.length() == 0) {
            println("unknown serverUrl")
            return
        }

        if (serverVersion.length() == 0) {
            println("unknown serverVersion")
            return
        }

        if (awsAutoScalingGroupName.length() == 0) {
            println("unknown awsAutoScalingGroupName")
            return
        }

        // delete if exists
        new File(CommonStrings.buildInstallPath(projectName.toString(), keystoreName.toString())).delete()
        new File(CommonStrings.buildInstallPath(projectName, truststoreName)).delete()

        // copy ssl files
        Files.copy(keystoreFile.toPath(), new File(CommonStrings.buildInstallPath(projectName.toString(), keystoreName.toString())).toPath())
        Files.copy(truststoreFile.toPath(), new File(CommonStrings.buildInstallPath(projectName, truststoreName)).toPath())

        new AutoScalingScriptBuilder(sshRSA, additionalAutoScalingSetup, projectName, CommonStrings.buildInstallPath(projectName, CommonStrings.AutoScalingName))
                .build()

        new AWSDesiredCapacityBuilder(awsAutoScalingGroupName, CommonStrings.buildInstallPath(projectName, CommonStrings.AWSDesiredCapacityName))
                .build()

        new CreateInstallPackageBuilder(serverVersion, projectName, CommonStrings.buildInstallPath(projectName, CommonStrings.CreateAndInstallPackageName))
                .build()

        new RunActorBuilder(projectName, serverUrl, CommonStrings.buildProjectStartActorScriptLocation(projectName), actorPort, seedNode1Port, seedNode2Port, false)
            .build()

        new RunServerBuilder(projectName, serverUrl, CommonStrings.buildProjectStartServerScriptLocation(projectName), managerPort, seedNode1Port, seedNode2Port, false)
            .build()

        new RunStopBuilder(CommonStrings.buildProjectStopScriptLocation(projectName), [actorPort, seedNode1Port, seedNode2Port, managerPort])
            .build()

        new StartupBuilder(fullServerTarUrl, CommonStrings.buildInstallPath(projectName, CommonStrings.StartupName))
            .build()

        buildDebianDirectoryStructure()

        new RunActorBuilder(projectName, serverUrl, CommonStrings.buildSystemStartActorScriptLocation(projectName), actorPort, seedNode1Port, seedNode2Port, true)
                .build()

        new RunServerBuilder(projectName, serverUrl, CommonStrings.buildSystemStartServerScriptLocation(projectName), managerPort, seedNode1Port, seedNode2Port, true)
                .build()

        new RunStopBuilder(CommonStrings.buildSystemStopScriptLocation(projectName), [actorPort, seedNode1Port, seedNode2Port, managerPort])
                .build()

        new CreateInstallPackageBuilder(serverVersion, projectName, "${CommonStrings.DebianBuild}${CommonStrings.CreateAndInstallPackageName}")
                .build()

        new ServiceBuilder(projectName, "${CommonStrings.DebianBuildBuild}etc/init.d/$projectName")
                .build()

        new AutoScalingScriptBuilder(sshRSA, additionalAutoScalingSetup, projectName, "${CommonStrings.DebianBuild}${CommonStrings.AutoScalingName}")
                .build()

        new DebianPackageBuilder(projectName, serverVersion, maintainerInfo, "${CommonStrings.DebianBuildBuild}DEBIAN/control")
                .build()

        project.copy {
            from "${CommonStrings.BuildInstallPath}$projectName"
            into "${CommonStrings.DebianBuildBuild}opt/$projectName"
        }
        project.copy {
            from CommonStrings.DebianBuildBuild
            into "${CommonStrings.DebianBuild}${projectName}_${serverVersion}_all"
            fileMode 0755
        }

        new File(CommonStrings.DebianBuildBuild).deleteDir()
    }

    private def buildDebianDirectoryStructure() {
        // make directory structure for debian package
        new File(CommonStrings.DebianBuild).deleteDir()
        new File(CommonStrings.DebianBuild).mkdir()
        new File(CommonStrings.DebianBuildBuild).mkdir()
        new File("${CommonStrings.DebianBuildBuild}usr").mkdir()
        new File("${CommonStrings.DebianBuildBuild}usr/sbin").mkdir()
        new File("${CommonStrings.DebianBuildBuild}opt").mkdir()
        new File("${CommonStrings.DebianBuildBuild}opt/$projectName").mkdir()
        new File("${CommonStrings.DebianBuildBuild}etc").mkdir()
        new File("${CommonStrings.DebianBuildBuild}etc/init.d").mkdir()
        new File("${CommonStrings.DebianBuildBuild}DEBIAN").mkdir()
    }
}
