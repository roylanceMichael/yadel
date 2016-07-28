export interface IHttpExecuteService {
    performPost(url:string, data:any, onSuccess:(data) => void, onError:(data) => void)
}