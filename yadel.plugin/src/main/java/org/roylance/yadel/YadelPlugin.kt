package org.roylance.yadel

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.roylance.yaclib.core.utilities.FileProcessUtilities
import java.io.File
import java.nio.file.Files

open class YadelPlugin: DefaultTask() {
  var projectName = ""
  var serverUrl = ""
  var serverVersion = ""

  var sshRSA = ""
  var additionalAutoScalingSetup = ""

  var awsAutoScalingGroupName = ""

  var keystoreName = ""
  var truststoreName = ""

  var fullServerTarUrl = ""
  var maintainerInfo = ""

  var actorPort = 2234
  var seedNode1Port = 2345
  var seedNode2Port = 2346
  var managerPort = 2344

  @TaskAction
  fun packageServer() {
    println("building hades with server: $serverUrl and serverVersion: $serverVersion")

    if (keystoreName.isEmpty()) {
      println("unknown keystore location")
      return
    }
    if (truststoreName.isEmpty()) {
      println("unknown trustore location")
      return
    }

    val keystoreFile = File("src/main/resources/$keystoreName")
    if (!keystoreFile.exists()) {
      println("keystore file does not exist")
    }

    val truststoreFile = File("src/main/resources/$truststoreName")
    if (!truststoreFile.exists()) {
      println("truststore file does not exist")
      return
    }

    if (projectName.isEmpty()) {
      println("unknown projectName")
      return
    }

    if (fullServerTarUrl.isEmpty()) {
      println("unknown fullServerTarUrl")
      return
    }

    if (maintainerInfo.isEmpty()) {
      println("unknown maintainerInfo")
      return
    }

    if (serverUrl.isEmpty()) {
      println("unknown serverUrl")
      return
    }

    if (serverVersion.isEmpty()) {
      println("unknown serverVersion")
      return
    }

    if (awsAutoScalingGroupName.isEmpty()) {
      println("unknown awsAutoScalingGroupName")
      return
    }

    // delete if exists
    File(CommonStrings.buildInstallPath(projectName.toString(), keystoreName.toString())).delete()
    File(CommonStrings.buildInstallPath(projectName, truststoreName)).delete()

    // copy ssl files
    Files.copy(keystoreFile.toPath(), File(CommonStrings.buildInstallPath(projectName, keystoreName)).toPath())
    Files.copy(truststoreFile.toPath(), File(CommonStrings.buildInstallPath(projectName, truststoreName)).toPath())

    AutoScalingScriptBuilder(sshRSA, additionalAutoScalingSetup, projectName, CommonStrings.buildInstallPath(projectName, CommonStrings.AutoScalingName))
    .build()

    AWSDesiredCapacityBuilder(awsAutoScalingGroupName, CommonStrings.buildInstallPath(projectName, CommonStrings.AWSDesiredCapacityName))
    .build()

    CreateInstallPackageBuilder(serverVersion, projectName, CommonStrings.buildInstallPath(projectName, CommonStrings.CreateAndInstallPackageName))
    .build()

    RunActorBuilder(projectName, serverUrl, CommonStrings.buildProjectStartActorScriptLocation(projectName), actorPort, seedNode1Port, seedNode2Port, false)
    .build()

    RunServerBuilder(projectName, serverUrl, CommonStrings.buildProjectStartServerScriptLocation(projectName), managerPort, seedNode1Port, seedNode2Port, false)
    .build()

    RunStopBuilder(CommonStrings.buildProjectStopScriptLocation(projectName), listOf(actorPort, seedNode1Port, seedNode2Port, managerPort))
    .build()

    StartupBuilder(fullServerTarUrl, CommonStrings.buildInstallPath(projectName, CommonStrings.StartupName))
    .build()

    buildDebianDirectoryStructure()

    RunActorBuilder(projectName, serverUrl, CommonStrings.buildSystemStartActorScriptLocation(projectName), actorPort, seedNode1Port, seedNode2Port, true)
    .build()

    RunServerBuilder(projectName, serverUrl, CommonStrings.buildSystemStartServerScriptLocation(projectName), managerPort, seedNode1Port, seedNode2Port, true)
    .build()

    RunStopBuilder(CommonStrings.buildSystemStopScriptLocation(projectName), listOf(actorPort, seedNode1Port, seedNode2Port, managerPort))
    .build()

    CreateInstallPackageBuilder(serverVersion, projectName, "${CommonStrings.DebianBuild}${CommonStrings.CreateAndInstallPackageName}")
    .build()

    ServiceBuilder(projectName, "${CommonStrings.DebianBuildBuild}etc/init.d/$projectName")
    .build()

    AutoScalingScriptBuilder(sshRSA, additionalAutoScalingSetup, projectName, "${CommonStrings.DebianBuild}${CommonStrings.AutoScalingName}")
    .build()

    DebianPackageBuilder(projectName, serverVersion, maintainerInfo, "${CommonStrings.DebianBuildBuild}DEBIAN/control")
    .build()

    project.copy {
      it.from("${CommonStrings.BuildInstallPath}$projectName")
      it.into("${CommonStrings.DebianBuildBuild}opt/$projectName")
    }

    project.copy {
      it.from(CommonStrings.DebianBuildBuild)
      it.into("${CommonStrings.DebianBuild}${projectName}_${serverVersion}_all")
      it.fileMode = 755
    }

    File(CommonStrings.DebianBuildBuild).deleteRecursively()

    val currentDir = System.getProperty("user.dir")

    FileProcessUtilities.executeProcess(currentDir, "dpkg-deb", "--build " + "${CommonStrings.DebianBuild}${projectName}_${serverVersion}_all")
    File("${CommonStrings.DebianBuild}${projectName}_${serverVersion}_all").deleteRecursively()

    project.tarTree(File(CommonStrings.DebianBuild))
  }

  private fun buildDebianDirectoryStructure() {
    // make directory structure for debian package
    File(CommonStrings.DebianBuild).deleteRecursively()
    File(CommonStrings.DebianBuild).mkdir()
    File(CommonStrings.DebianBuildBuild).mkdir()
    File("${CommonStrings.DebianBuildBuild}usr").mkdir()
    File("${CommonStrings.DebianBuildBuild}usr/sbin").mkdir()
    File("${CommonStrings.DebianBuildBuild}opt").mkdir()
    File("${CommonStrings.DebianBuildBuild}opt/$projectName").mkdir()
    File("${CommonStrings.DebianBuildBuild}etc").mkdir()
    File("${CommonStrings.DebianBuildBuild}etc/init.d").mkdir()
    File("${CommonStrings.DebianBuildBuild}DEBIAN").mkdir()
  }
}
