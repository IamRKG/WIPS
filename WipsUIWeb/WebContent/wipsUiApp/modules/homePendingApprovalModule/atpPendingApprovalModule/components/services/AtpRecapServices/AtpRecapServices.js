'use strict';
angular.module('WipsUiApp.ATPPendingApproval.ComponentsModule')
	.service('ATPRecapServices', ['WcHttpEndpointPrototype','$q','AtpRecapPrototype','WcAlertConsoleService', '$translate','$state','User','WipsHttpStatusCodesServices','WipsHttpStatusCodesConstant',function(WcHttpEndpointPrototype,$q,AtpRecapPrototype,WcAlertConsoleService,$translate, $state,User,WipsHttpStatusCodesServices,WipsHttpStatusCodesConstant) {

		this.atpRecapEndpoint = new WcHttpEndpointPrototype('AtpApproval/Recap');
		
		this.getATPRecap = function(ltermToken,atpNumber) {
			var urlEndCode = encodeURIComponent(ltermToken)+'/'+encodeURIComponent(atpNumber);
			
			return this.atpRecapEndpoint.subRoute(urlEndCode).get().then(angular.bind(this, function(response) {
				if(response.status != WipsHttpStatusCodesConstant.statusTextAccepted){
					response = new AtpRecapPrototype(response);
					return response;
					}else{
					WcAlertConsoleService.addMessage({
						message:response.errorMessage,
						type: 'danger',
						multiple: false
					});
					return $q.reject(response);
				}

			}), function(failure) {
				if(failure.status == WipsHttpStatusCodesConstant.conflict409){
					var param = {
							categoryCode: User.currentCategoryCode,
							ltermToken:User.userInformation.ltermToken
					};
					$state.go('atp-pending-approval',param,{reload: true});
					WcAlertConsoleService.addMessage({
						message:failure.data.errorMessage,
						type: 'danger',
						multiple: false
					});
				}
				else{
					WipsHttpStatusCodesServices.getWipsHttpStatusCodes(failure)
				}
				return $q.reject(failure);
			});
		};
		

	}]);
