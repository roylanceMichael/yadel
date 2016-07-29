"use strict";
var MainController = (function () {
    function MainController(reportService, dagService, protobufBuilder) {
        this.dagReportNameSpace = "org.roylance.yadel.api.models.UIDagReport";
        this.reportService = reportService;
        this.dagService = dagService;
        this.protobufBuilder = protobufBuilder;
        this.refresh();
    }
    MainController.prototype.refresh = function () {
        var self = this;
        this.selectedDag = null;
        this.currentDagReport = null;
        var request = new this.protobufBuilder.UIRequest();
        this.reportService.current(request, function (response) {
            self.currentDagReport = response.report;
            if (self.currentDagReport.dags.length > 0) {
                self.selectedDag = self.currentDagReport.dags[0];
                self.onDagChange();
            }
        }, function (response) {
            self.onDagChange();
        });
    };
    MainController.prototype.onDagChange = function () {
        if (this.selectedDag) {
            this.dagService.buildTreeVisualization(this.selectedDag);
        }
    };
    MainController.prototype.deleteDag = function () {
        if (this.selectedDag) {
            // delete the dag
            var self_1 = this;
            var request = new this.protobufBuilder.UIRequest();
            request.setRequestType(1 /* DELETE_DAG */);
            request.dag_id = this.selectedDag.id;
            this.reportService.delete_dag(request, function (response) {
                self_1.refresh();
            }, function (data) {
                console.log(data);
                self_1.refresh();
            });
        }
    };
    return MainController;
}());
exports.MainController = MainController;
//# sourceMappingURL=main.js.map