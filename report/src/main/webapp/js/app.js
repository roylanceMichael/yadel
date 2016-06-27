var app = angular.module('jsapp', []);
app.factory('urlService', function($window) {
    return {
        protoMessageUrl:$window.location.pathname + "rest/proto/message",
        reportUrl:$window.location.pathname + "rest/report/current"
    };
});

app.controller("mainController", ["$scope", "$http", "urlService", function($scope, $http, urlService) {
    $scope.builder = null;
    $scope.reportBuilder = null;
    $scope.report = "nothing yet";

    $http.get(urlService.protoMessageUrl)
        .success(function(data) {
            $scope.report = "now waiting on report... : " + data;
            $scope.builder = dcodeIO.ProtoBuf.loadProto(data);
            $scope.reportBuilder = $scope.builder.build("org.roylance.yadel.api.models.UIDagReport")

            $http.get(urlService.reportUrl)
                .success(function(data) {
                    var foundReport = $scope.reportBuilder.decode64(data);

                    var arrange = "LR";

                    var g = new dagreD3.graphlib.Graph();
                    g.setGraph({});
                    g.setDefaultEdgeLabel(function() { return {}; });

                    for(var dagIdx in foundReport.dags) {
                        var foundDag = foundReport.dags[dagIdx];

                        for (var nodeIdx in foundDag.nodes.map) {
                            var node = foundDag.nodes.map[nodeIdx];
                            if (node.value.is_completed) {
                                g.setNode(node.key, { label: node.key, style: "fill: green" });
                            }
                            else {
                                g.setNode(node.key, { label: node.key, style: "fill: whitesmoke" });
                            }
                        }

                        for (var edgeIdx in foundDag.edges) {
                            g.setEdge(foundDag.edges[edgeIdx].node_id_2,  foundDag.edges[edgeIdx].node_id_1);
                        }
                    }

                    var render = new dagreD3.render();
                    var svg = d3.select("svg"), svgGroup = svg.append("g");
                    render(d3.select("svg g"), g);
                })
                .error(function(data) {
                    console.log(data);
                });
        })
        .error(function(data) {
            console.log(data);
        });
}]);
