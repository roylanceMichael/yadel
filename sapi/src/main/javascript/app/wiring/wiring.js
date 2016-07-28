"use strict";
var model_factories_1 = require("../../node_modules/yadeljs/model_factories");
var ReportService_1 = require("../../node_modules/yadeljs/ReportService");
var local_url_1 = require("../services/local.url");
var dag_1 = require("../services/dag");
var http_execute_1 = require("../services/http.execute");
var main_1 = require("../controllers/main");
var localUrlService = new local_url_1.LocalUrlService();
var mainControllerName = "mainController";
var app = angular.module('jsapp', [
    "ngRoute"
]);
app.config(function ($routeProvider) {
    $routeProvider
        .when(localUrlService.main, {
        templateUrl: "views/main.html",
        controller: mainControllerName
    })
        .otherwise({
        redirectTo: localUrlService.main
    });
});
app.factory("yadelModel", function () {
    return model_factories_1.YadelModel.org.roylance.yadel;
});
app.factory("dagService", function () {
    return new dag_1.DagService();
});
app.factory("httpExecuteService", function ($window, $http) {
    return new http_execute_1.HttpExecuteService($http);
});
app.factory("reportService", function (httpExecuteService, yadelModel) {
    return new ReportService_1.ReportService(httpExecuteService, yadelModel);
});
app.controller(mainControllerName, ['$scope', '$http', '$log', 'httpExecuteService', 'dagService', 'reportService', 'yadelModel',
    function ($scope, $http, $log, httpExecuteService, dagService, reportService, yadelModel) {
        $scope.i = new main_1.MainController(reportService, dagService, yadelModel);
    }]);
//# sourceMappingURL=wiring.js.map