'use strict';

angular.module('WipsUiApp.ComponentsModule')
.directive('wipsList', function() {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, element, attr, ngModel) {
        	
            function fromUser(text) {
                return text.split("\n");
            }

            
            function toUser(array) {
            	if(array != ''){
                    return array.join("\n");
            	}else{
            		
            		return undefined;
            	}
            
            }

            ngModel.$parsers.push(fromUser);
            ngModel.$formatters.push(toUser);
        }
    };
});