import {IDagService} from "../services/all";
import {IReportService} from "org.roylance.yadel.api/IReportService";
import {org} from "org.roylance.yadel.api/YadelModel";
declare var dcodeIO:any;
declare var console:any;

export class MainController {
    dagReportNameSpace = "org.roylance.yadel.api.models.UIDagReport";

    reportService: IReportService;
    dagService:IDagService;

    currentDagReport:org.roylance.yadel.IUIDagReport;
    selectedDag:org.roylance.yadel.IUIDag;

    constructor(reportService: IReportService, dagService:IDagService) {
        this.reportService = reportService;
        this.dagService = dagService;
        this.refresh();
    }

    refresh() {
        const self = this;
        this.selectedDag = null;
        this.currentDagReport = null;

        const request = new org.roylance.yadel.UIYadelRequest();
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
            const request = new org.roylance.yadel.UIYadelRequest();
            request.requestType = org.roylance.yadel.UIYadelRequestType.DELETE_DAG;
            request.dagId = this.selectedDag.id;
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