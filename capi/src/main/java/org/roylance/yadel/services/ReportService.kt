package org.roylance.yadel.services

import org.roylance.common.service.IProtoSerializerService

class ReportService(
        private val restReport: IReportRest,
        private val protoSerializer: IProtoSerializerService): IReportService {

    override fun delete(request: org.roylance.yadel.YadelReport.UIRequest): org.roylance.yadel.YadelReport.UIResponse {
        val base64request = protoSerializer.serializeToBase64(request)
        val responseCall = restReport.delete(base64request)
        val response = responseCall.execute()
        return protoSerializer.deserializeFromBase64(response.body(),
                org.roylance.yadel.YadelReport.UIResponse.getDefaultInstance())
    }

    override fun current(request: org.roylance.yadel.YadelReport.UIRequest): org.roylance.yadel.YadelReport.UIResponse {
        val base64request = protoSerializer.serializeToBase64(request)
        val responseCall = restReport.current(base64request)
        val response = responseCall.execute()
        return protoSerializer.deserializeFromBase64(response.body(),
                org.roylance.yadel.YadelReport.UIResponse.getDefaultInstance())
    }
}