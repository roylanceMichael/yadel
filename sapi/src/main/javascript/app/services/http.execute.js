"use strict";
var HttpExecuteService = (function () {
    function HttpExecuteService(httpService) {
        this.httpPost = "POST";
        this.httpService = httpService;
    }
    HttpExecuteService.prototype.performPost = function (url, data, onSuccess, onError) {
        this.httpService({
            url: url,
            method: this.httpPost,
            data: data
        }).success(onSuccess)
            .error(onError);
    };
    return HttpExecuteService;
}());
exports.HttpExecuteService = HttpExecuteService;
//# sourceMappingURL=http.execute.js.map