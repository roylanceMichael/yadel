package org.roylance.yadel.services

interface IReportService {
	fun delete_dag(request: org.roylance.yadel.YadelReport.UIRequest): org.roylance.yadel.YadelReport.UIResponse
	fun current(request: org.roylance.yadel.YadelReport.UIRequest): org.roylance.yadel.YadelReport.UIResponse
}