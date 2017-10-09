package org.roylance.yadel.utilities

import org.roylance.yadel.YadelModel
import org.roylance.yadel.services.IActor
import org.roylance.yadel.services.ITaskLogger
import java.util.*

object TestUtilities {
    fun getSystemTaskLogger(): ITaskLogger {
        return object: ITaskLogger {
            override val logs: List<YadelModel.Log>
                get() = ArrayList()

            override fun clearLogs() {
            }

            override fun debug(message: String) {
                println(message)
            }

            override fun error(message: String) {
                println(message)
            }

            override fun info(message: String) {
                println(message)
            }
        }
    }

    fun getTestActor(): IActor {
        return object: IActor {
            override fun forward(message: Any) {

            }

            override fun tell(message: Any) {

            }

            override fun key(): String {
                return "report"
            }

            override fun host(): String {
                return ""
            }

            override fun port(): String {
                return ""
            }

        }
    }
}