package org.roylance.yadel

import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.roylance.yaclib.core.utilities.FileProcessUtilities

class YadelServerPlugin extends DefaultTask {
    public String currentProjectName;
    public String projectName;
    public String serverVersion;
    public String seedHost;

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
        if (seedHost == null || seedHost.size() == 0) {
            println("no seedHost listed, exiting...")
            return
        }

        new File("build").delete()

        def properties = new Properties()
        def stream = new FileInputStream("../" + projectName + "/gradle.properties")
        properties.load(stream)
        properties.setProperty("seedHost", seedHost)
        stream.close()

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