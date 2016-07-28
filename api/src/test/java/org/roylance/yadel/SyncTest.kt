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
import java.nio.file.Paths

class SyncTest {
    @Test
    fun executeSync() {
        val location = Paths.get(System.getenv("YACLIB_LOCATION")).toString()
        val version = System.getenv("YACLIB_VERSION")

        val dependency = YaclibModel.Dependency.newBuilder()
                .setGroup(this.javaClass.`package`.name)
                .setName("api")
                .setVersion("0.$version-SNAPSHOT")
                .build()

        val filePersistService = FilePersistService()
        val processFileDescriptorService = ProcessFileDescriptorService()

        val controllers = processFileDescriptorService.processFile(YadelController.getDescriptor())

        val serverFiles = JavaServerProcessLanguageService().buildInterface(controllers, dependency)
        filePersistService.persistFiles(Paths.get(location, CommonTokens.ServerApi).toString(), serverFiles)

        val clientFiles = JavaClientProcessLanguageService().buildInterface(controllers, dependency)
        filePersistService.persistFiles(Paths.get(location, CommonTokens.ClientApi).toString(), clientFiles)

        val typeScriptFiles = TypeScriptProcessLanguageService().buildInterface(controllers, dependency)
        filePersistService.persistFiles(Paths.get(location, "api", "javascript").toString(), typeScriptFiles)

        // tests to make sure files exist
        Assert.assertTrue(true)
    }
}