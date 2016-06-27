import {IDagService} from "./all";
import {UIDag} from "../models/all"

declare var d3:any;
declare var dagreD3:any;

export class DagService implements IDagService {
    buildTreeVisualization(dag:UIDag) {
        const arrange = "LR";

        const g = new dagreD3.graphlib.Graph();
        g.setGraph({});
        g.setDefaultEdgeLabel(function() { return {}; });

        for (const nodeIdx in dag.nodes.map) {
            var node = dag.nodes.map[nodeIdx];
            if (node.value.is_completed) {
                g.setNode(node.key, { label: node.key, style: "fill: green" });
            }
            else if (node.value.is_processing) {
                g.setNode(node.key, { label: node.key, style: "fill: gray" });
            }
            else if (node.value.is_error) {
                g.setNode(node.key, { label: node.key, style: "fill: red" });
            }
            else {
                g.setNode(node.key, { label: node.key, style: "fill: whitesmoke" });
            }
        }

        for (var edgeIdx in dag.edges) {
            g.setEdge(dag.edges[edgeIdx].node_id_2,  dag.edges[edgeIdx].node_id_1);
        }

        const render = new dagreD3.render();
        // todo: parameterize this more
        const svg = d3.select("svg"), svgGroup = svg.append("g");
        render(d3.select("svg g"), g);
    }
}