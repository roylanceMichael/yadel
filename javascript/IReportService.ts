export interface IReportService {
	delete_dag(request: org.roylance.yadel.UIYadelRequest, onSuccess:(response: org.roylance.yadel.UIYadelResponse)=>void, onError:(response:any)=>void)
	current(request: org.roylance.yadel.UIYadelRequest, onSuccess:(response: org.roylance.yadel.UIYadelResponse)=>void, onError:(response:any)=>void)
}