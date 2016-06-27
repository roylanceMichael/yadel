import {IDagService} from "./all";
import {UIDagReport} from "../models/all"

declare var d3:any;
declare var dagreD3:any;

export class DagService implements IDagService {
    buildTreeVisualization(dagReport:UIDagReport, svgId:string) {
        const arrange = "LR";

        const g = new dagreD3.graphlib.Graph();
        g.setGraph({});
        g.setDefaultEdgeLabel(function() { return {}; });

        for(const dagIdx in dagReport.dags) {
            const foundDag = dagReport.dags[dagIdx];

            for (const nodeIdx in foundDag.nodes.map) {
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
        const render = new dagreD3.render();
        // todo: parameterize this more
        const svg = d3.select("svg"), svgGroup = svg.append("g");
        render(d3.select("svg g"), g);
    }
}