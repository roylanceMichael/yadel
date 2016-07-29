"use strict";
var ReportService = (function () {
    function ReportService(httpExecute, modelFactory) {
        this.httpExecute = httpExecute;
        this.modelFactory = modelFactory;
    }
    ReportService.prototype.delete_dag = function (request, onSuccess, onError) {
        var self = this;
        this.httpExecute.performPost("/rest/report/delete-dag", request.toBase64(), function (result) {
            onSuccess(self.modelFactory.UIResponse.decode64(result));
        }, onError);
    };
    ReportService.prototype.current = function (request, onSuccess, onError) {
        var self = this;
        this.httpExecute.performPost("/rest/report/current", request.toBase64(), function (result) {
            onSuccess(self.modelFactory.UIResponse.decode64(result));
        }, onError);
    };
    return ReportService;
}());
exports.ReportService = ReportService;
