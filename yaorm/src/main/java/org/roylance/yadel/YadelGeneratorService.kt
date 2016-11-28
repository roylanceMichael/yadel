package org.roylance.yadel

import com.google.protobuf.GeneratedMessageV3
import org.roylance.yaorm.services.proto.BaseProtoGeneratedMessageBuilder

object YadelGeneratorService : BaseProtoGeneratedMessageBuilder() {
    override val name: String
        get() = "YadelModel"

    override fun buildGeneratedMessage(name: String): GeneratedMessageV3 {
        if (YadelModel.Log.getDescriptor().name == name) {
            return YadelModel.Log.getDefaultInstance()
        }
        if (YadelModel.Dag.getDescriptor().name == name) {
            return YadelModel.Dag.getDefaultInstance()
        }
        if (YadelModel.Task.getDescriptor().name == name) {
            return YadelModel.Task.getDefaultInstance()
        }
        if (YadelModel.TaskDependency.getDescriptor().name == name) {
            return YadelModel.TaskDependency.getDefaultInstance()
        }
        if (YadelModel.AddTaskToDag.getDescriptor().name == name) {
            return YadelModel.AddTaskToDag.getDefaultInstance()
        }
        if (YadelModel.CompleteTask.getDescriptor().name == name) {
            return YadelModel.CompleteTask.getDefaultInstance()
        }

        return super.buildGeneratedMessage(name)
    }
}