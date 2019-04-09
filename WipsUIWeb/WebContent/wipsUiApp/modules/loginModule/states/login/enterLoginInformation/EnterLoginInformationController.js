'use strict';

angular.module('WipsUiApp.LoginModule')
	.controller('EnterLoginInformationController', ['$scope','SwitchJobFactory', 'WcHttpRequestService', 'ApplicationPolicyService','User','WcAlertConsoleService','$state','SessionServices','WipsUtilityServices',
		function($scope, SwitchJobFactory,WcHttpRequestService, ApplicationPolicyService,User,WcAlertConsoleService,$state,SessionServices,WipsUtilityServices) {

		$scope.wipsUiAppController.userInformation = {};
		
		$scope.wipsUiAppController.isMenuVislbe = $state.current.name;
		
		WcHttpRequestService.configuration.headers.Authorization = 'Login';
		
		this.getUserInformation = function() {
			if($scope.$parent.loginForm.$invalid){
				this.loginFormValidation();
			}
			else{
				var param = {
						racfId: this.loginId,
						racfPassword: this.loginPassword
				};
				
				WcAlertConsoleService.removeErrorMessages();
				
				return 	ApplicationPolicyService.getUserInformation(param).then(angular.bind(this, function(userInfo) {
					User.setCachedUserInfo(userInfo);
					SwitchJobFactory.jobCode=undefined;
					SwitchJobFactory.jobName=undefined;
					$scope.wipsUiAppController.userInformation = userInfo;
					WipsUtilityServices.redirectiveListPage(userInfo);
					return userInfo;
				}));
			}
		};

		/*Validation*/
        this.loginFormValidation = function () {
            this.isFormSubmitted = false;
            if ($scope.$parent.loginForm.$valid) {
            } else {
                this.isFormSubmitted = true;
            }
        };
        
		
	  }]);
