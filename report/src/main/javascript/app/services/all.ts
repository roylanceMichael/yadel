import {UIDag} from "../models/all";
export interface IDagService {
    buildTreeVisualization(dag:UIDag);
}

export interface IHttpExecuteService {
    httpGet:string;
    httpPost:string;
    httpPut:string;
    httpPatch:string;
    httpDelete:string;

    getExecute(url:string, data: any, onSuccess:(data:any)=>void, onError:(data:any)=>void);
    postExecute(url:string, data: any, onSuccess:(data:any)=>void, onError:(data:any)=>void);
    deleteExecute(url:string, data: any, onSuccess:(data:any)=>void, onError:(data:any)=>void);
}

export interface IUrlService {
    protoMessageUrl:string;
    reportUrl:string;
    deleteDagUrl:string;
}

export interface ILocalUrlService {
    main:string
}
