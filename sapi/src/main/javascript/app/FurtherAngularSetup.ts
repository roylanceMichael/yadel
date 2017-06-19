import {IDagService, ILocalUrlService} from "./services/all";
import {LocalUrlService} from "./services/local.url";
import {MainController} from "./controllers/main";
import {ReportService} from "org.roylance.yadel.api/ReportService";
import {HttpExecute} from "./HttpExecute"
import {DagService} from "./services/dag";
import {org} from "org.roylance.yadel.api/YadelModel";

// this file won't be overwritten, add more dependencies for angular as needed
export function furtherAngularSetup(app:any) {
    const localUrlService = new LocalUrlService();
    const mainControllerName = "mainController";

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

    app.factory("dagService", function ($window, $http) {
        return new DagService();
    });

    app.controller(mainControllerName, ['$scope', '$http', '$log', 'httpExecute', 'dagService', 'reportService',
    function($scope, $http, $log, httpExecute:HttpExecute, dagService:IDagService, reportService: ReportService) {
        $scope.i = new MainController(reportService, dagService);
    }]);
}
