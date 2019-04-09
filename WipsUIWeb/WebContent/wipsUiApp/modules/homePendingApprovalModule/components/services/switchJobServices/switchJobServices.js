angular.module('WipsUiApp.HomePendingApproval.ComponentsModule')
	.service('SwitchJobServices', ['WcHttpEndpointPrototype','$q','$translate', 'WcAlertConsoleService','SessionServices','$state','User',function(WcHttpEndpointPrototype,$q,$translate,WcAlertConsoleService,SessionServices,$state,User) {

		this.altJobCodeEndpoint = new WcHttpEndpointPrototype('DelegateJobCode');
		
		this.getJobCodeList = function(param) {
			return this.altJobCodeEndpoint.post({jobCode: param.jobCode, jobName: param.jobName, ltermToken: param.ltermToken}).then(angular.bind(this,function(response){
				if(response.status == '202'){
					WcAlertConsoleService.addMessage({
					message:response.data.errorMessage,
					type: 'danger',
					multiple: false
					});
					return $q.reject(response);                     
					}
				return $q.when(response.data);
			}), function(failure) {
				if(failure.status == '401'){
					$state.go('login.enter-login-information');
					User.getClearUserInformation();
					WcAlertConsoleService.addMessage({
						message:$translate.instant('sessionTimeOut.message'),
						type: 'danger',
						multiple: false
					});
					
				}else if(failure.status == '500'){
					WcAlertConsoleService.addMessage({
						message:$translate.instant('application.errors.internalServerError'),
						type: 'danger',
						multiple: false
					});
				}
				else{
					WcAlertConsoleService.addMessage({
						message:failure.data.errorMessage,
						type: 'danger',
						multiple: false
					});
				}
	        	return $q.when(failure.data);
			});
		};
	}]);