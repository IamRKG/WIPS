'use strict';

angular.module('WipsUiApp.ComponentsModule').
	service('ApplicationPolicyService', ['UserService','WcAlertConsoleService','$q',
		function(UserService,WcAlertConsoleService,$q) {

		
		this.getUserInformation = function(param) {
			return UserService.userInformationEndpoint.post({racfId: param.racfId, racfPassword: param.racfPassword}).then(angular.bind(this,function(response){
				
				return $q.when(response.data);
			}), function(failure) {
				WcAlertConsoleService.addMessage({
					message:failure.data.errorMessage,
					type: 'danger',
					multiple: false
				});
	        	return $q.reject(failure);
			});
		};

		}]);