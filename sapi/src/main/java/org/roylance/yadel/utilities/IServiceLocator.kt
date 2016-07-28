package org.roylance.yadel.utilities

interface IServiceLocator {
    val protobufSerializerService: org.roylance.common.service.IProtoSerializerService
	val reportService: org.roylance.yadel.services.IReportService
}
