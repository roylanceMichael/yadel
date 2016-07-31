import {IDagService} from "../services/all";
import {IReportService} from "../../node_modules/org.roylance.yadel.api/IReportService";
import ProtoBufBuilder = org.roylance.yadel.ProtoBufBuilder;
declare var dcodeIO:any;
declare var console:any;

export class MainController {
    dagReportNameSpace = "org.roylance.yadel.api.models.UIDagReport";

    protobufBuilder:ProtoBufBuilder;
    reportService: IReportService;
    dagService:IDagService;

    currentDagReport:org.roylance.yadel.UIDagReport;
    selectedDag:org.roylance.yadel.UIDag;

    constructor(reportService: IReportService, dagService:IDagService, protobufBuilder:ProtoBufBuilder) {
        this.reportService = reportService;
        this.dagService = dagService;
        this.protobufBuilder = protobufBuilder;
        this.refresh();
    }

    refresh() {
        const self = this;
        this.selectedDag = null;
        this.currentDagReport = null;

        const request = new this.protobufBuilder.UIYadelRequest();
        this.reportService.current(request, function(response: org.roylance.yadel.UIYadelResponse) {
            self.currentDagReport = response.report;
            if (self.currentDagReport.dags.length > 0) {
                self.selectedDag = self.currentDagReport.dags[0];
                self.onDagChange();
            }

        }, function(response:any) {
            self.onDagChange();
        });
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
            const request = new this.protobufBuilder.UIYadelRequest();
            request.setRequestType(org.roylance.yadel.UIYadelRequestType.DELETE_DAG);
            request.dag_id = this.selectedDag.id;
            this.reportService.delete_dag(request,
                function(response: org.roylance.yadel.UIYadelResponse) {
                    self.refresh();
                },
                function(data) {
                    console.log(data);
                    self.refresh();
                });
        }
    }
}