import {ILocalUrlService} from "./all";
export class LocalUrlService implements ILocalUrlService {
    main:string;
    constructor() {
        this.main = "/main";
    }
}