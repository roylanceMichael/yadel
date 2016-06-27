import {IHttpExecuteService} from "./all"

export class HttpExecuteService implements IHttpExecuteService {
    csrfTokenName = "X-CSRFToken";
    httpGet:string = "GET";
    httpPost:string = "POST";
    httpPut:string = "PUT";
    httpPatch:string = "PATCH";
    httpDelete:string = "DELETE";

    httpService:any;

    constructor(httpService:any) {
        this.httpService = httpService;
    }

    getExecute(url:string, data:any, onSuccess:(data:any)=>void, onError:(data:any)=>void) {
        this.httpService({
            url: url,
            method: this.httpGet,
            data: data,
        })
            .success(function(data) {
                onSuccess(data);
            })
            .error(function(data) {
                onError(data);
            });
    }

    postExecute(url:string, data:any, onSuccess:(data:any)=>void, onError:(data:any)=>void) {
        this.httpService({
            url: url,
            method: this.httpPost,
            data: data,
        })
            .success(function(data) {
                onSuccess(data);
            })
            .error(function(data) {
                onError(data);
            });
    }

    deleteExecute(url:string, data:any, onSuccess:(data:any)=>void, onError:(data:any)=>void) {
        this.httpService({
            url: url,
            method: this.httpDelete,
            data: data,
        })
            .success(function(data) {
                onSuccess(data);
            })
            .error(function(data) {
                onError(data);
            });
    }
}