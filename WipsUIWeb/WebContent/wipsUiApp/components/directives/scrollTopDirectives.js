'use strict';

angular.module('WipsUiApp.ComponentsModule')
    .directive('scrollTop', function () {
    return {
        restrict: 'A',
 link: function(scope, element, attr) {
	 document.body.scrollTop = 1;
        }
    };
}); 