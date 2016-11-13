"use strict";
// THIS FILE WAS AUTO-GENERATED. DO NOT ALTER!
/// <reference path="../node_modules/roylance.common/bytebuffer.d.ts" />
var YadelModelFactory_1 = require("../node_modules/org.roylance.yadel.api/YadelModelFactory");
var ReportService_1 = require("../node_modules/org.roylance.yadel.api/ReportService");
var HttpExecute_1 = require("./HttpExecute");
var FurtherAngularSetup_1 = require("./FurtherAngularSetup");
var app = angular.module('jsapp', [
    "ngRoute"
]);
app.factory("httpExecute", function ($window, $http) {
    return new HttpExecute_1.HttpExecute($http);
});
app.factory("yadelModel", function () {
    return YadelModelFactory_1.YadelModel.org.roylance.yadel;
});
app.factory("reportService", function (httpExecute, yadelModel) {
    return new ReportService_1.ReportService(httpExecute, yadelModel);
});
FurtherAngularSetup_1.furtherAngularSetup(app);
//# sourceMappingURL=Wiring.js.map