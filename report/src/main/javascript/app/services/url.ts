import {IUrlService} from "./all";
export class UrlService implements IUrlService {
    protoMessageUrl:string;
    reportUrl:string;
    deleteDagUrl:string;
    constructor(window:any) {
        var pathLocation = window.location.pathname + "rest";
        this.protoMessageUrl = pathLocation + "/proto/message";
        this.reportUrl = pathLocation + "/report/current";
        this.deleteDagUrl = pathLocation + "/report/delete";
    }
}