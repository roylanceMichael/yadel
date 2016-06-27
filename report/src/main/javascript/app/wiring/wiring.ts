import {IUrlService, IDagService, IHttpExecuteService, ILocalUrlService} from "../services/all";
import {LocalUrlService} from "../services/local.url"
import {DagService} from "../services/dag";
import {UrlService} from "../services/url";
import {HttpExecuteService} from "../services/http.execute";
import {MainController} from "../controllers/main";

declare var angular: any;
const localUrlService = new LocalUrlService();
const mainControllerName = "mainController";

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

app.factory("dagService", function() {
    return new DagService();
});

app.factory("urlService", function ($window) {
    return new UrlService($window);
});

app.factory("httpExecuteService", function ($window, $http) {
    return new HttpExecuteService($http);
});

app.controller(mainControllerName, ['$scope', '$http', '$log', 'urlService', 'httpExecuteService', 'dagService',
    function($scope, $http, $log, urlService: IUrlService,httpExecuteService:IHttpExecuteService, dagService:IDagService) {
        $scope.i = new MainController(urlService, httpExecuteService, dagService);
    }]);