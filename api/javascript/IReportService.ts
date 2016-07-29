/// <reference path="./YadelModel" />
export interface IReportService {
	delete_dag(request: org.roylance.yadel.UIRequest, onSuccess:(response: org.roylance.yadel.UIResponse)=>void, onError:(response:any)=>void)
	current(request: org.roylance.yadel.UIRequest, onSuccess:(response: org.roylance.yadel.UIResponse)=>void, onError:(response:any)=>void)
}