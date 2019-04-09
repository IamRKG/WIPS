'use strict';
angular.module('WipsUiApp.ATPPendingApproval.ComponentsModule')
.service('logOutServices',['WcHttpEndpointPrototype','$q','WipsHttpStatusCodesConstant',function(WcHttpEndpointPrototype,$q,WipsHttpStatusCodesConstant){
	
	this.logOutServicesEndpoint = new WcHttpEndpointPrototype('WipsLogOut');
	
	this.getLogOut = function(ltermToken){
		var urlEndCode = encodeURIComponent(ltermToken);
			return this.logOutServicesEndpoint.subRoute(urlEndCode).get().then(angular.bind(this, function(response) {
				if(response.status != WipsHttpStatusCodesConstant.statusTextAccepted){
					return response;
				}else{
					WcAlertConsoleService.addMessage({
						message: $translate.instant(response.errorMessage),
						type: 'danger',
						multiple: false
					});
					return $q.reject(response);
				}
			}), function(failure) {
				 if(failure.status == WipsHttpStatusCodesConstant.internalServerError500){
					WcAlertConsoleService.addMessage({
						message:$translate.instant('application.errors.internalServerError'),
						type: 'danger',
						multiple: false
					});
				}
				return $q.reject(failure);
			});
		};
}]);