export interface IHttpExecute {
    performPost(url:string, data:any, onSuccess:(data) => void, onError:(data) => void)
}