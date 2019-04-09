'use strict';

angular.module('WipsUiApp.LoginModule')
	.controller('LoginController', ['$scope','UserService',
		function($scope, UserService) {
		
		/*this.getUserInformation = function() {
			
			if($scope.loginForm.$invalid){
				this.loginFormValidation();
			}
			else{
				var param = {
					loginId: this.loginId,
					loginPassword: this.loginPassword
				};
		
				return 	UserService.getUserInformation(param).then(angular.bind(this, function(userInfo) {
					
				}));
			}
		};*/
	
		 /*Validation*/
        /*this.loginFormValidation = function () {
            this.isFormSubmitted = false;
            if ($scope.loginForm.$valid) {
            } else {
                this.isFormSubmitted = true;
            }
        };*/
		
	}]);
