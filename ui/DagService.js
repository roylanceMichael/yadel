"use strict";
var DagService = (function () {
    function DagService() {
        this.successColor = "green";
        this.errorColor = "red";
        this.processingColor = "gray";
        this.notProcessedColor = "whitesmoke";
    }
    DagService.prototype.buildTreeVisualization = function (dag, dagController) {
        var arrange = "TB";
        var g = new dagreD3.graphlib.Graph();
        g.setGraph({
            rankdir: "LR"
        });
        g.setDefaultEdgeLabel(function () { return {}; });
        for (var nodeIdx in dag.nodes) {
            var node = dag.nodes[nodeIdx];
            if (node.getIsCompleted()) {
                g.setNode(node.getId(), { label: node.getDisplay(), style: "fill: " + this.successColor });
            }
            else if (node.getIsProcessing()) {
                g.setNode(node.getId(), { label: node.getDisplay(), style: "fill: " + this.processingColor });
            }
            else if (node.getIsError()) {
                g.setNode(node.getId(), { label: node.getDisplay(), style: "fill: " + this.errorColor });
            }
            else {
                g.setNode(node.getId(), { label: node.getDisplay(), style: "fill: " + this.notProcessedColor });
            }
        }
        for (var edgeIdx in dag.edges) {
            g.setEdge(dag.edges[edgeIdx].node_id_2, dag.edges[edgeIdx].node_id_1);
        }
        var render = new dagreD3.render();
        var svg = d3.select("svg"), svgGroup = svg.append("g");
        svg.call(d3.behavior.zoom().on("zoom", function () {
            var ev = d3.event;
            svg.select("g")
                .attr("transform", "translate(" + ev.translate + ") scale(" + ev.scale + ")");
        }));
        render(d3.select("svg g"), g);
        d3.selectAll("g.node")
            .on({
            "mouseover": function (d) {
                d3.select(this).style("cursor", "pointer");
            },
            "mouseout": function (d) {
                d3.select(this).style("cursor", "default");
            },
            "click": function (d) {
                dagController.selectNode(d.toString());
            }
        });
    };
    return DagService;
}());
exports.DagService = DagService;
//# sourceMappingURL=DagService.js.map