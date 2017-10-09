// THIS FILE WAS AUTO-GENERATED. DO NOT ALTER!
/// <reference path="../node_modules/roylance.common/bytebuffer.d.ts" />
import {YadelModel} from "../node_modules/org.roylance.yadel/YadelModelFactory";

import {ReportService} from "../node_modules/org.roylance.yadel/ReportService";

import {HttpExecute} from "./HttpExecute"
import {furtherAngularSetup} from "./FurtherAngularSetup"

declare var angular: any;
const app = angular.module('jsapp', [
    "ngRoute"
]);

app.factory("httpExecute", function ($window, $http) {
    return new HttpExecute($http);
});

app.factory("yadelModel", function () {
    return YadelModel.org.roylance;
});

app.factory("reportService", function(httpExecute:HttpExecute, yadelModel:org.roylance.yadel.ProtoBufBuilder) {
    return new ReportService(httpExecute, yadelModel)
});


furtherAngularSetup(app);
