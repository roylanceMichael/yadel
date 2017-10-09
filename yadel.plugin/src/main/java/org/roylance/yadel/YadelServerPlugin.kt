package org.roylance.yadel

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.Properties
import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.roylance.yaclib.core.utilities.FileProcessUtilities

open class YadelServerPlugin : DefaultTask() {
  var currentProjectName: String? = null

  var projectName: String? = ""
  var serverUrl: String? = ""
  var serverVersion: String? = ""

  var awsAutoScalingGroupName: String? = ""
  var fullServerTarUrl: String? = ""

  @TaskAction
  @Throws(IOException::class)
  fun packageServer() {
    if (currentProjectName == null || currentProjectName!!.length == 0) {
      println("server project not listed, exiting...")
      return
    }
    if (projectName == null || projectName!!.length == 0) {
      println("yadel project not listed, exiting...")
      return
    }
    if (serverUrl == null || serverUrl!!.length == 0) {
      println("no serverUrl listed, exiting...")
      return
    }
    if (serverVersion == null || serverVersion!!.length == 0) {
      println("no serverVersion listed, exiting...")
      return
    }
    if (awsAutoScalingGroupName == null || awsAutoScalingGroupName!!.length == 0) {
      println("no awsAutoScalingGroupName listed, exiting...")
      return
    }
    if (fullServerTarUrl == null || fullServerTarUrl!!.length == 0) {
      println("no fullServerTarUrl listed, exiting...")
      return
    }

    File("build").delete()
    val properties = Properties()
    val propertiesFile = File("../$projectName/gradle.properties")
    val stream = FileInputStream(propertiesFile)
    properties.load(stream)
    properties.setProperty("serverUrl_", serverUrl)
    properties.setProperty("awsAutoScalingGroupName_", awsAutoScalingGroupName)
    properties.setProperty("fullServerTarUrl_", fullServerTarUrl)
    stream.close()

    val outputStream = FileOutputStream(propertiesFile)
    properties.store(outputStream,
        "set serverUrl_: \${serverUrl}, awsAutoScalingGroupName_: \${awsAutoScalingGroupName}, fullServerTarUrl_: \${fullServerTarUrl}")
    outputStream.close()

    FileProcessUtilities.executeProcess("../" + projectName!!, "gradle", "clean")
    FileProcessUtilities.executeProcess("../" + projectName!!, "gradle", "tarApp")

    File("src/main/resources/server.tar").delete()
    FileUtils.copyFile(
        File("../$projectName/build/distributions/\${projectName}-\${serverVersion}.tar"),
        File("src/main/resources/server.tar"))
    FileUtils.copyFile(
        File("../$projectName/build/debian/\${projectName}_\${serverVersion}_all.deb"),
        File("\${projectName}_\${serverVersion}_all.deb"))

    FileProcessUtilities.executeProcess(".", "gradle", "clean")
    FileProcessUtilities.executeProcess(".", "gradle", "packageApp")

    FileUtils.copyFile(File("build/debian/\${currentProjectName}_\${serverVersion}_all.deb"),
        File("\${currentProjectName}_\${serverVersion}_all.deb"))
  }
}
