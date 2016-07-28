"use strict";
var ReportService = (function () {
    function ReportService(httpExecuteService, modelFactory) {
        this.httpExecuteService = httpExecuteService;
        this.modelFactory = modelFactory;
    }
    ReportService.prototype.delete = function (request, onSuccess, onError) {
        var self = this;
        this.httpExecuteService.performPost("/rest/report/delete", request.toBase64(), function (result) {
            onSuccess(self.modelFactory.UIResponse.decode64(result));
        }, onError);
    };
    ReportService.prototype.current = function (request, onSuccess, onError) {
        var self = this;
        this.httpExecuteService.performPost("/rest/report/current", request.toBase64(), function (result) {
            onSuccess(self.modelFactory.UIResponse.decode64(result));
        }, onError);
    };
    return ReportService;
}());
exports.ReportService = ReportService;
