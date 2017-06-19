import {org} from "org.roylance.yadel.api/YadelModel"

export interface IDagService {
    buildTreeVisualization(dag:org.roylance.yadel.IUIDag);
}

export interface ILocalUrlService {
    main:string
}
