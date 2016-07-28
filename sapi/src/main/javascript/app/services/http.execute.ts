import {IHttpExecuteService} from "../../node_modules/yadeljs/IHttpExecute"

export class HttpExecuteService implements IHttpExecuteService {
    httpPost:string = "POST";
    httpService:any;

    constructor(httpService:any) {
        this.httpService = httpService;
    }

    performPost(url:string, data:any, onSuccess:(data)=>void, onError:(data)=>void) {
        this.httpService({
            url: url,
            method: this.httpPost,
            data: data
        }).success(onSuccess)
          .error(onError);
    }
}