// THIS FILE WAS AUTO-GENERATED. DO NOT ALTER!
import {ReportService} from "org.roylance.yadel.api/ReportService";

import {HttpExecute} from "./HttpExecute"
import {furtherAngularSetup} from "./FurtherAngularSetup"

declare var angular: any;
const app = angular.module('jsapp', [
    "ngRoute"
]);

app.factory("httpExecute", function ($window, $http) {
    return new HttpExecute($http);
});

app.factory("reportService", function(httpExecute:HttpExecute) {
    return new ReportService(httpExecute);
});


furtherAngularSetup(app);
