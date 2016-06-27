export interface IProtoBuilder<T> {
    decode64(path:String):T
}

export interface UIDagReport {
    dags:UIDag[]
}

export interface UIDag {
    id:string
    display:string
    nodes:Map<UINode>
    edges:UIEdge[]
    is_completed:boolean
    is_processing:boolean
    is_error:boolean
    logs:string[]
}

export interface UIEdge {
    node_id_1:string
    node_id_2:string
}

export interface UINode {
    id:string
    display:string
    execution_date:number
    start_date:number
    end_date:number
    duration:number
    is_completed:boolean
    is_processing:boolean
    is_error:boolean
    logs:string[]
}

export interface UIRequest {
    request_type:number
    dag_id:string
}

export class UIRequests {
    report_dags:number = 1;
    delete_dag:number = 2;
}

export interface Map<T> {
    map:KvP<T>[]
}

export interface KvP<T> {
    key:string
    value:T
}