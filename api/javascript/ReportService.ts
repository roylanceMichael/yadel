import {IReportService} from "./IReportService";
import {IHttpExecuteService} from "./IHttpExecute";
import ProtoBufBuilder = org.roylance.yadel.ProtoBufBuilder;

export class ReportService implements IReportService {
    httpExecuteService:IHttpExecuteService;
    modelFactory:ProtoBufBuilder;

    constructor(httpExecuteService:IHttpExecuteService,
                modelFactory:ProtoBufBuilder) {
        this.httpExecuteService = httpExecuteService;
        this.modelFactory = modelFactory;
    }
	delete(request: org.roylance.yadel.UIRequest, onSuccess:(response: org.roylance.yadel.UIResponse)=>void, onError:(response:any)=>void) {
            const self = this;
            this.httpExecuteService.performPost("/rest/report/delete",
                    request.toBase64(),
                    function(result:string) {
                        onSuccess(self.modelFactory.UIResponse.decode64(result));
                    },
                    onError);
        }
	current(request: org.roylance.yadel.UIRequest, onSuccess:(response: org.roylance.yadel.UIResponse)=>void, onError:(response:any)=>void) {
            const self = this;
            this.httpExecuteService.performPost("/rest/report/current",
                    request.toBase64(),
                    function(result:string) {
                        onSuccess(self.modelFactory.UIResponse.decode64(result));
                    },
                    onError);
        }
}