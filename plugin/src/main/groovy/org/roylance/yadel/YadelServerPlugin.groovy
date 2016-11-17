package org.roylance.yadel

import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.roylance.yaclib.core.utilities.FileProcessUtilities

class YadelServerPlugin extends DefaultTask {
    public String currentProjectName;

    public String projectName = ""
    public String serverUrl = ""
    public String serverVersion = ""

    public String awsAutoScalingGroupName = ""
    public String fullServerTarUrl = ""


    @TaskAction
    def packageServer() {
        if (currentProjectName == null || currentProjectName.size() == 0) {
            println("server project not listed, exiting...")
            return
        }
        if (projectName == null || projectName.size() == 0) {
            println("yadel project not listed, exiting...")
            return
        }
        if (serverUrl == null || serverUrl.size() == 0) {
            println("no serverUrl listed, exiting...")
            return
        }
        if (serverVersion == null || serverVersion.size() == 0) {
            println("no serverVersion listed, exiting...")
            return
        }
        if (awsAutoScalingGroupName == null || awsAutoScalingGroupName.size() == 0) {
            println("no awsAutoScalingGroupName listed, exiting...")
            return
        }
        if (fullServerTarUrl == null || fullServerTarUrl.size() == 0) {
            println("no fullServerTarUrl listed, exiting...")
            return
        }

        new File("build").delete()

        def properties = new Properties()
        def propertiesFile = new File("../" + projectName + "/gradle.properties")
        def stream = new FileInputStream(propertiesFile)
        properties.load(stream)
        properties.setProperty("serverUrl_", serverUrl)
        properties.setProperty("awsAutoScalingGroupName_", awsAutoScalingGroupName)
        properties.setProperty("fullServerTarUrl_", fullServerTarUrl)
        stream.close()

        def outputStream = new FileOutputStream(propertiesFile)
        properties.store(outputStream, "set serverUrl_: ${serverUrl}, awsAutoScalingGroupName_: ${awsAutoScalingGroupName}, fullServerTarUrl_: ${fullServerTarUrl}")
        outputStream.close()

        FileProcessUtilities.INSTANCE.executeProcess("../" + projectName, "gradle", "clean")
        FileProcessUtilities.INSTANCE.executeProcess("../" + projectName, "gradle", "tarApp")

        new File("src/main/resources/server.tar").delete()
        FileUtils.copyFile(new File("../" + projectName + "/build/distributions/${projectName}-${serverVersion}.tar"), new File("src/main/resources/server.tar"))
        FileUtils.copyFile(new File("../" + projectName + "/build/debian/${projectName}_${serverVersion}_all.deb"), new File("${projectName}_${serverVersion}_all.deb"))

        FileProcessUtilities.INSTANCE.executeProcess(".", "gradle", "clean")
        FileProcessUtilities.INSTANCE.executeProcess(".", "gradle", "packageApp")

        FileUtils.copyFile(new File("build/debian/${currentProjectName}_${serverVersion}_all.deb"), new File("${currentProjectName}_${serverVersion}_all.deb"))
    }
}
