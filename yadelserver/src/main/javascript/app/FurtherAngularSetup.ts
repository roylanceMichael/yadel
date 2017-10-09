// This file was auto-generated, but can be altered. It will not be overwritten.
declare var $:any;
export function furtherAngularSetup(app:any) {

    // including show-tab in there, I find this useful for angular
    app.directive('showTab', function () {
            return {
                link: function (scope, element, attrs) {
                    element.click(function (e) {
                        e.preventDefault();
                        $(element).tab('show');
                    });
                }
            };
        });
}
