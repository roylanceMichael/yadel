import {YadelModel} from "../node_modules/org.roylance.yadel.api/YadelModelFactory";

import {ReportService} from "../node_modules/org.roylance.yadel.api/ReportService";

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
    return YadelModel.org.roylance.yadel;
});

app.factory("reportService", function(httpExecute:HttpExecute, yadelModel:org.roylance.yadel.ProtoBufBuilder) {
    return new ReportService(httpExecute, yadelModel)
});


furtherAngularSetup(app);
