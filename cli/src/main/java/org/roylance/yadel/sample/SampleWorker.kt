package org.roylance.yadel.sample

import org.roylance.yadel.api.actors.WorkerBase
import org.roylance.yadel.api.models.YadelModels

class SampleWorker:WorkerBase() {
    override fun onReceive(p0: Any?) {
        this.log.info("received message... child")
        super.onReceive(p0)
        if(p0 is YadelModels.ManagerToWorkerMessage) {
            if (p0.type.equals(YadelModels.ManagerToWorkerMessageType.START_WORKING)) {
                this.log.info("received message to start working!")
                val responseMessage = YadelModels.WorkerToManagerMessage.newBuilder()
                responseMessage.name = p0.name
                this.log.info("telling manager that we're working!")
                this.startWorking(responseMessage)

                val workspace = StringBuilder()
                var i = 0;
                while (i < 50) {
                    System.out.println("working on $i!")
                    workspace.appendln(i)
                    Thread.sleep(500)
                    i++
                }

                val newContext = responseMessage.addContextBuilder()
                newContext.value = YadelModels.WorkerToManagerMessage.newBuilder().setName("yoyoyo").build().toByteString()
                this.log.info("telling manager that we're done working!")
                this.finishWorking(responseMessage)
            }
        }
    }
}
