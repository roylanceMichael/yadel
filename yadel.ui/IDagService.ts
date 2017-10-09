import {IDagController} from "./IDagController";

export interface IDagService {
	successColor:string;
	errorColor:string;
	processingColor:string;
	notProcessedColor:string;
    buildTreeVisualization(dag: org.roylance.yadel.UIDag, dagController: IDagController);
}