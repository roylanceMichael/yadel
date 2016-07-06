package org.roylance.yadel.api.utilities

import com.google.protobuf.GeneratedMessage
import org.roylance.yadel.api.models.YadelModels
import org.roylance.yaorm.services.proto.IProtoGeneratedMessageBuilder

class YadelModelsProtoGenerator:IProtoGeneratedMessageBuilder {
    override fun buildGeneratedMessage(name: String): GeneratedMessage {
        if (YadelModels.WorkerConfiguration.getDescriptor().name.equals(name)) {
            return YadelModels.WorkerConfiguration.getDefaultInstance()
        }
        else if (YadelModels.Log.getDescriptor().name.equals(name)) {
            return YadelModels.Log.getDefaultInstance()
        }
        else if (YadelModels.Dag.getDescriptor().name.equals(name)) {
            return YadelModels.Dag.getDefaultInstance()
        }
        else if (YadelModels.Task.getDescriptor().name.equals(name)) {
            return YadelModels.Task.getDefaultInstance()
        }
        else if (YadelModels.AddTaskToDag.getDescriptor().name.equals(name)) {
            return YadelModels.AddTaskToDag.getDefaultInstance()
        }
        else if (YadelModels.CompleteTask.getDescriptor().name.equals(name)) {
            return YadelModels.CompleteTask.getDefaultInstance()
        }
        else if (YadelModels.TaskDependency.getDescriptor().name.equals(name)) {
            return YadelModels.TaskDependency.getDefaultInstance()
        }

        throw UnsupportedOperationException("name not found: $name") //To change body of created functions use File | Settings | File Templates.
    }
}
