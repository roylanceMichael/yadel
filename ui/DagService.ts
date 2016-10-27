/// <reference path="node_modules/org.roylance.yadel.api/YadelModel.d.ts" />
import {IDagController} from "./IDagController";
import {IDagService} from "./IDagService";

declare var d3:any;
declare var dagreD3:any;

export class DagService implements IDagService {
	successColor:string = "green";
	errorColor:string = "red";
	processingColor:string = "gray";
	notProcessedColor:string = "whitesmoke";

	buildTreeVisualization(dag: org.roylance.yadel.UIDag, dagController: IDagController) {
		const arrange = "TB";

        const g = new dagreD3.graphlib.Graph();
        g.setGraph({
            rankdir: "LR"
        });
        g.setDefaultEdgeLabel(function() { return {}; });

        for (const nodeIdx in dag.nodes) {
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
            g.setEdge(dag.edges[edgeIdx].node_id_2,  dag.edges[edgeIdx].node_id_1);
        }

        const render = new dagreD3.render();
        
        const svg = d3.select("svg"), svgGroup = svg.append("g");
        svg.call(d3.behavior.zoom().on("zoom", function() {
            var ev = d3.event;
            svg.select("g")
                .attr("transform", "translate(" + ev.translate + ") scale(" + ev.scale + ")");
        }));

        render(d3.select("svg g"), g);
        d3.selectAll("g.node")
            .on({
                "mouseover": function(d) {
                    d3.select(this).style("cursor", "pointer");
                },
                "mouseout": function(d) {
                    d3.select(this).style("cursor", "default");
                },
                "click": function(d) {
                    dagController.selectNode(d.toString());
                }
            });
	}
}