import {IDagService, ILocalUrlService} from "../services/all";
import {YadelModel} from "../../node_modules/yadeljs/model_factories"
import {ReportService} from "../../node_modules/yadeljs/ReportService"
import {LocalUrlService} from "../services/local.url"
import {DagService} from "../services/dag";
import {HttpExecuteService} from "../services/http.execute";
import {MainController} from "../controllers/main";

declare var angular: any;
const localUrlService = new LocalUrlService();
const mainControllerName = "mainController";

const app = angular.module('jsapp', [
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
    return YadelModel.org.roylance.yadel;
});

app.factory("dagService", function() {
    return new DagService();
});

app.factory("httpExecuteService", function ($window, $http) {
    return new HttpExecuteService($http);
});

app.factory("reportService", function(httpExecuteService:HttpExecuteService, yadelModel:org.roylance.yadel.ProtoBufBuilder) {
    return new ReportService(httpExecuteService, yadelModel)
});


app.controller(mainControllerName, ['$scope', '$http', '$log', 'httpExecuteService', 'dagService', 'reportService', 'yadelModel',
    function($scope, $http, $log, httpExecuteService:HttpExecuteService, dagService:IDagService, reportService: ReportService, yadelModel: org.roylance.yadel.ProtoBufBuilder) {
        $scope.i = new MainController(reportService, dagService, yadelModel);
    }]);