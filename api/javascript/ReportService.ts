import {IReportService} from "./IReportService";
import {IHttpExecute} from "./IHttpExecute";
import ProtoBufBuilder = org.roylance.yadel.ProtoBufBuilder;

export class ReportService implements IReportService {
    httpExecute:IHttpExecute;
    modelFactory:ProtoBufBuilder;

    constructor(httpExecute:IHttpExecute,
                modelFactory:ProtoBufBuilder) {
        this.httpExecute = httpExecute;
        this.modelFactory = modelFactory;
    }
	delete_dag(request: org.roylance.yadel.UIRequest, onSuccess:(response: org.roylance.yadel.UIResponse)=>void, onError:(response:any)=>void) {
            const self = this;
            this.httpExecute.performPost("/rest/report/delete-dag",
                    request.toBase64(),
                    function(result:string) {
                        onSuccess(self.modelFactory.UIResponse.decode64(result));
                    },
                    onError);
        }
	current(request: org.roylance.yadel.UIRequest, onSuccess:(response: org.roylance.yadel.UIResponse)=>void, onError:(response:any)=>void) {
            const self = this;
            this.httpExecute.performPost("/rest/report/current",
                    request.toBase64(),
                    function(result:string) {
                        onSuccess(self.modelFactory.UIResponse.decode64(result));
                    },
                    onError);
        }
}