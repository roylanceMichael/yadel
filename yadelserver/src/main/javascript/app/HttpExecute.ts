// This file was auto-generated, but can be altered. It will not be overwritten.
import {IHttpExecute} from "../node_modules/org.roylance.yadel/IHttpExecute"

export class HttpExecute implements IHttpExecute {
    httpPost:string = "POST";
    httpService:any;

    constructor(httpService:any) {
        this.httpService = httpService;
    }

    performPost(url:string, data:any, onSuccess:(data)=>void, onError:(data)=>void) {
        // https://docs.angularjs.org/api/ng/service/$http
        this.httpService({
            url: url,
            method: this.httpPost,
            data: data
        }).then(function(response) {
            onSuccess(response.data);
        },
            function(response) {
            onError(response.data);
        });
    }
}