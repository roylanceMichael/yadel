package org.roylance.yadel.api.services

import org.roylance.common.service.ILogger
import org.roylance.yadel.YadelModel

interface ITaskLogger: ILogger {
    val logs: List<YadelModel.Log>
    fun clearLogs()
    fun debug(message:String)
}