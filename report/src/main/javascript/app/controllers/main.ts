import {IUrlService, IHttpExecuteService, IDagService} from "../services/all";
import {IProtoBuilder, UIDagReport} from "../models/all";
declare var dcodeIO:any;
declare var console:any;

export class MainController {
    urlService:IUrlService;
    httpExecute:IHttpExecuteService;
    dagService:IDagService;
    dagReportBuilder:IProtoBuilder<UIDagReport>;

    dagReportNameSpace = "org.roylance.yadel.api.models.UIDagReport";

    constructor(urlService:IUrlService, httpExecute:IHttpExecuteService, dagService:IDagService) {
        this.urlService = urlService;
        this.httpExecute = httpExecute;
        this.dagService = dagService;

        const self = this;
        this.httpExecute.getExecute(this.urlService.protoMessageUrl,
            null,
            function(data:any) {
                self.handleProto(data);
        }, function(data:any) {
            console.log(data);
        })
    }

    handleProto(data:any) {
        const builder = dcodeIO.ProtoBuf.loadProto(data);
        this.dagReportBuilder = builder.build(this.dagReportNameSpace);

        const self = this;
        this.httpExecute.getExecute(this.urlService.reportUrl, null, function(data:any) {
            self.handleReport(data);
        }, function(data) {
            console.log(data);
        });
    }

    handleReport(data:any) {
        const dagReport = this.dagReportBuilder.decode64(data);
        this.dagService.buildTreeVisualization(dagReport, "");
    }
}