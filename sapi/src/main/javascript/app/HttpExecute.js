"use strict";
var HttpExecute = (function () {
    function HttpExecute(httpService) {
        this.httpPost = "POST";
        this.httpService = httpService;
    }
    HttpExecute.prototype.performPost = function (url, data, onSuccess, onError) {
        this.httpService({
            url: url,
            method: this.httpPost,
            data: data
        }).success(onSuccess)
            .error(onError);
    };
    return HttpExecute;
}());
exports.HttpExecute = HttpExecute;
//# sourceMappingURL=HttpExecute.js.map