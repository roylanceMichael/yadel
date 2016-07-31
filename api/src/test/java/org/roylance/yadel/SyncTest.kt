package org.roylance.yadel

import org.junit.Assert
import org.junit.Test
import org.roylance.yaclib.YaclibModel
import org.roylance.yaclib.core.enums.CommonTokens
import org.roylance.yaclib.core.services.FilePersistService
import org.roylance.yaclib.core.services.ProcessFileDescriptorService
import org.roylance.yaclib.core.services.java.client.JavaClientProcessLanguageService
import org.roylance.yaclib.core.services.java.server.JavaServerProcessLanguageService
import org.roylance.yaclib.core.services.typescript.TypeScriptProcessLanguageService
import org.roylance.yaclib.core.utilities.TypeScriptUtilities
import java.nio.file.Paths
import java.util.*

class SyncTest {
    @Test
    fun executeSync() {
        val typescriptLocation = System.getenv("TYPESCRIPT_MODEL_FILE_NAME") ?: return

        val location = Paths.get(System.getenv("YACLIB_LOCATION")).toString()
        val version = System.getenv("YACLIB_VERSION").toInt()


        val dependency = YaclibModel.Dependency.newBuilder()
                .setGroup(this.javaClass.`package`.name)
                .setName("api")
                .setVersion(version)
                .setTypescriptModelFile(typescriptLocation)
                .build()

        val filePersistService = FilePersistService()
        val processFileDescriptorService = ProcessFileDescriptorService()

        val controllers = processFileDescriptorService.processFile(YadelController.getDescriptor())

        val controllerDependency = YaclibModel.ControllerDependency.newBuilder().setDependency(dependency).setControllers(controllers)
                .build()

        val allControllerDependencies = YaclibModel.AllControllerDependencies.newBuilder().addControllerDependencies(controllerDependency).build()

        val akkaDependency = YaclibModel.Dependency.newBuilder().setType(YaclibModel.DependencyType.JAVA)
                .setGroup("com.typesafe.akka")
                .setName("akka-cluster_2.11")
                .setThirdPartyDependencyVersion("2.4.7")
                .build()

        val thirdPartyDependencies = ArrayList<YaclibModel.Dependency>()
        thirdPartyDependencies.addAll(TypeScriptUtilities.baseServerTypeScriptKit)
        thirdPartyDependencies.add(akkaDependency)

        val serverFiles = JavaServerProcessLanguageService().buildInterface(allControllerDependencies, dependency, thirdPartyDependencies)
        filePersistService.persistFiles(Paths.get(location, CommonTokens.ServerApi).toString(), serverFiles)

        val clientFiles = JavaClientProcessLanguageService().buildInterface(allControllerDependencies, dependency)
        filePersistService.persistFiles(Paths.get(location, CommonTokens.ClientApi).toString(), clientFiles)

        val typeScriptFiles = TypeScriptProcessLanguageService().buildInterface(allControllerDependencies, dependency)
        filePersistService.persistFiles(Paths.get(location, CommonTokens.JavaScriptName).toString(), typeScriptFiles)

        // tests to make sure files exist
        Assert.assertTrue(true)
    }
}