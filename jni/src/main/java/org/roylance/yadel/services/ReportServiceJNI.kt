// THIS FILE WAS AUTO-GENERATED. DO NOT ALTER!
package org.roylance.yadel.services;

class ReportServiceJNI: IReportService {
    private val bridge = ReportJNIBridge()
	override fun delete_dag(request: org.roylance.yadel.YadelReport.UIYadelRequest): org.roylance.yadel.YadelReport.UIYadelResponse {
		val bytes = request.toByteArray()
		val result = this.bridge.delete_dag(bytes)
		return org.roylance.yadel.YadelReport.UIYadelResponse.parseFrom(result)
	}
	override fun current(request: org.roylance.yadel.YadelReport.UIYadelRequest): org.roylance.yadel.YadelReport.UIYadelResponse {
		val bytes = request.toByteArray()
		val result = this.bridge.current(bytes)
		return org.roylance.yadel.YadelReport.UIYadelResponse.parseFrom(result)
	}
	override fun get_dag_status(request: org.roylance.yadel.YadelReport.UIYadelRequest): org.roylance.yadel.YadelReport.UIYadelResponse {
		val bytes = request.toByteArray()
		val result = this.bridge.get_dag_status(bytes)
		return org.roylance.yadel.YadelReport.UIYadelResponse.parseFrom(result)
	}
}
