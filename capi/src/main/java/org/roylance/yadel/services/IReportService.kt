package org.roylance.yadel.services

interface IReportService {
	fun delete_dag(request: org.roylance.yadel.YadelReport.UIYadelRequest): org.roylance.yadel.YadelReport.UIYadelResponse
	fun current(request: org.roylance.yadel.YadelReport.UIYadelRequest): org.roylance.yadel.YadelReport.UIYadelResponse
}