package org.roylance.yadel.utilities

import org.roylance.yadel.services.Base64Service
import org.roylance.yadel.services.ReportService

object ServiceLocator: IServiceLocator {
    override val protobufSerializerService: org.roylance.common.service.IProtoSerializerService
        get() = org.roylance.common.service.ProtoSerializerService(Base64Service())
    override val reportService: org.roylance.yadel.services.IReportService
        get() = ReportService()

}
