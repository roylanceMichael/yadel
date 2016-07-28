package org.roylance.yadel.services

import akka.pattern.Patterns
import akka.util.Timeout
import org.roylance.yadel.YadelReport
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.duration.FiniteDuration

class ReportService: IReportService {
    override fun delete(request: YadelReport.UIRequest): YadelReport.UIResponse {
        val future = Patterns.ask(
                ActorSingleton.getManager(),
                request,
                this.create500Seconds())
        Await.result(future, create500SecondsDuration())
        return YadelReport.UIResponse.newBuilder().setContent("success").build()
    }

    override fun current(request: YadelReport.UIRequest): YadelReport.UIResponse {
        val future = Patterns.ask(
                ActorSingleton.getManager(),
                YadelReport.UIRequests.REPORT_DAGS,
                this.create500Seconds())

        val result = Await.result(future, create500SecondsDuration()) as YadelReport.UIDagReport

        return YadelReport.UIResponse.newBuilder()
                .setReport(result).build()
    }

    private fun create500Seconds(): Timeout {
        return Timeout(create500SecondsDuration())
    }

    private fun create500SecondsDuration():FiniteDuration {
        return Duration.create(500, "seconds")
    }
}