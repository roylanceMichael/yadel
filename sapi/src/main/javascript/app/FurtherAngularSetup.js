"use strict";
var local_url_1 = require("./services/local.url");
var main_1 = require("./controllers/main");
var dag_1 = require("./services/dag");
// this file won't be overwritten, add more dependencies for angular as needed
function furtherAngularSetup(app) {
    var localUrlService = new local_url_1.LocalUrlService();
    var mainControllerName = "mainController";
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
        return new dag_1.DagService();
    });
    app.controller(mainControllerName, ['$scope', '$http', '$log', 'httpExecute', 'dagService', 'reportService', 'yadelModel',
        function ($scope, $http, $log, httpExecute, dagService, reportService, yadelModel) {
            $scope.i = new main_1.MainController(reportService, dagService, yadelModel);
        }]);
}
exports.furtherAngularSetup = furtherAngularSetup;
//# sourceMappingURL=FurtherAngularSetup.js.map