import {IUrlService, IHttpExecuteService, IDagService} from "../services/all";
import {IProtoBuilder, UIDagReport, UIDag} from "../models/all";
declare var dcodeIO:any;
declare var console:any;

export class MainController {
    dagReportNameSpace = "org.roylance.yadel.api.models.UIDagReport";

    urlService:IUrlService;
    httpExecute:IHttpExecuteService;
    dagService:IDagService;
    dagReportBuilder:IProtoBuilder<UIDagReport>;

    currentDagReport:UIDagReport;
    selectedDag:UIDag;

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
        this.refresh();
    }

    refresh() {
        const self = this;
        this.selectedDag = null;
        this.currentDagReport = null;
        this.httpExecute.getExecute(this.urlService.reportUrl, null, function(data:any) {
            self.handleReport(data);
        }, function(data) {
            console.log(data);
        });
    }

    handleReport(data:any) {
        this.currentDagReport = this.dagReportBuilder.decode64(data);
        if (this.currentDagReport.dags.length > 0) {
            this.selectedDag = this.currentDagReport.dags[0];
            this.onDagChange();
        }
    }

    onDagChange() {
        if (this.selectedDag) {
            this.dagService.buildTreeVisualization(this.selectedDag);
        }
    }

    deleteDag() {
        if (this.selectedDag) {
            // delete the dag
            const self = this;
            this.httpExecute
                .postExecute(this.urlService.deleteDagUrl + "/" + this.selectedDag.id,
                    null,
                    function(data:any) {
                        self.refresh();
                    },
                    function(data:any) {
                        self.refresh();
                        console.log(data);
                    });
        }
    }
}