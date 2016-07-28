"use strict";
var DagService = (function () {
    function DagService() {
    }
    DagService.prototype.buildTreeVisualization = function (dag) {
        var arrange = "LR";
        var g = new dagreD3.graphlib.Graph();
        g.setGraph({});
        g.setDefaultEdgeLabel(function () { return {}; });
        for (var nodeIdx in dag.nodes) {
            var node = dag.nodes[nodeIdx];
            if (node.is_completed) {
                g.setNode(node.id, { label: node.display, style: "fill: green" });
            }
            else if (node.is_processing) {
                g.setNode(node.id, { label: node.display, style: "fill: gray" });
            }
            else if (node.is_error) {
                g.setNode(node.id, { label: node.display, style: "fill: red" });
            }
            else {
                g.setNode(node.id, { label: node.display, style: "fill: whitesmoke" });
            }
        }
        for (var edgeIdx in dag.edges) {
            g.setEdge(dag.edges[edgeIdx].node_id_2, dag.edges[edgeIdx].node_id_1);
        }
        var render = new dagreD3.render();
        // todo: parameterize this more
        var svg = d3.select("svg"), svgGroup = svg.append("g");
        render(d3.select("svg g"), g);
    };
    return DagService;
}());
exports.DagService = DagService;
//# sourceMappingURL=dag.js.map