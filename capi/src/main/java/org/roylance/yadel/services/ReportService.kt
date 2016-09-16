// THIS FILE WAS AUTO-GENERATED. DO NOT ALTER!
package org.roylance.yadel.services

import org.roylance.common.service.IProtoSerializerService

class ReportService(
        private val restReport: IReportRest,
        private val protoSerializer: IProtoSerializerService): IReportService {

    override fun delete_dag(request: org.roylance.yadel.YadelReport.UIYadelRequest): org.roylance.yadel.YadelReport.UIYadelResponse {
        val base64request = protoSerializer.serializeToBase64(request)
        val responseCall = restReport.delete_dag(base64request)
        val response = responseCall.execute()
        return protoSerializer.deserializeFromBase64(response.body(),
                org.roylance.yadel.YadelReport.UIYadelResponse.getDefaultInstance())
    }

    override fun current(request: org.roylance.yadel.YadelReport.UIYadelRequest): org.roylance.yadel.YadelReport.UIYadelResponse {
        val base64request = protoSerializer.serializeToBase64(request)
        val responseCall = restReport.current(base64request)
        val response = responseCall.execute()
        return protoSerializer.deserializeFromBase64(response.body(),
                org.roylance.yadel.YadelReport.UIYadelResponse.getDefaultInstance())
    }
}