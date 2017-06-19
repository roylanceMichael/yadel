import {IDagService} from "./all";
import {org} from "org.roylance.yadel.api/YadelModel";

declare var d3:any;
declare var dagreD3:any;

export class DagService implements IDagService {
    buildTreeVisualization(dag:org.roylance.yadel.IUIDag) {
        const arrange = "LR";

        const g = new dagreD3.graphlib.Graph();
        g.setGraph({});
        g.setDefaultEdgeLabel(function() { return {}; });

        for (const nodeIdx in dag.nodes) {
            var node = dag.nodes[nodeIdx];
            if (node.isCompleted) {
                g.setNode(node.id, { label: node.display, style: "fill: green" });
            }
            else if (node.isProcessing) {
                g.setNode(node.id, { label: node.display, style: "fill: gray" });
            }
            else if (node.isError) {
                g.setNode(node.id, { label: node.display, style: "fill: red" });
            }
            else {
                g.setNode(node.id, { label: node.display, style: "fill: whitesmoke" });
            }
        }

        for (var edgeIdx in dag.edges) {
            g.setEdge(dag.edges[edgeIdx].nodeId_2,  dag.edges[edgeIdx].nodeId_1);
        }

        const render = new dagreD3.render();
        // todo: parameterize this more
        const svg = d3.select("svg"), svgGroup = svg.append("g");
        //noinspection TypeScriptValidateTypes
        render(d3.select("svg g"), g);
    }
}