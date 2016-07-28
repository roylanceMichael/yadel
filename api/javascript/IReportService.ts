/// <reference path="./model" />
export interface IReportService {
	delete(request: org.roylance.yadel.UIRequest, onSuccess:(response: org.roylance.yadel.UIResponse)=>void, onError:(response:any)=>void)
	current(request: org.roylance.yadel.UIRequest, onSuccess:(response: org.roylance.yadel.UIResponse)=>void, onError:(response:any)=>void)
}