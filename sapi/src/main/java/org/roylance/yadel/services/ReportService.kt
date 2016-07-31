package org.roylance.yadel.services

import akka.pattern.Patterns
import akka.util.Timeout
import org.roylance.yadel.YadelReport
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.duration.FiniteDuration

class ReportService: IReportService {
    override fun delete_dag(request: YadelReport.UIYadelRequest): YadelReport.UIYadelResponse {
        val future = Patterns.ask(
                ActorSingleton.getManager(),
                request,
                this.create500Seconds())
        Await.result(future, create500SecondsDuration())
        return YadelReport.UIYadelResponse.newBuilder().setContent("success").build()
    }

    override fun current(request: YadelReport.UIYadelRequest): YadelReport.UIYadelResponse {
        val future = Patterns.ask(
                ActorSingleton.getManager(),
                YadelReport.UIYadelRequestType.REPORT_DAGS,
                this.create500Seconds())

        val result = Await.result(future, create500SecondsDuration()) as YadelReport.UIDagReport

        return YadelReport.UIYadelResponse.newBuilder()
                .setReport(result).build()
    }

    private fun create500Seconds(): Timeout {
        return Timeout(create500SecondsDuration())
    }

    private fun create500SecondsDuration():FiniteDuration {
        return Duration.create(500, "seconds")
    }
}