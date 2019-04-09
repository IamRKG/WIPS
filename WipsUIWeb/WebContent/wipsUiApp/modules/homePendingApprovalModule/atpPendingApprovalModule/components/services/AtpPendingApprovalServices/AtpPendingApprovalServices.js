'use strict';
angular.module('WipsUiApp.ATPPendingApproval.ComponentsModule')
	.service('ATPPendingApprovalServices', ['WcHttpEndpointPrototype','$q','WcAlertConsoleService', '$translate','$state','User','WipsHttpStatusCodesServices','WipsHttpStatusCodesConstant',function(WcHttpEndpointPrototype,$q,WcAlertConsoleService, $translate, $state,User,WipsHttpStatusCodesServices,WipsHttpStatusCodesConstant) {

		this.pendingApprovalEndpoint = new WcHttpEndpointPrototype('PendingApprovals');
		
		this.getATPPendingApproval = function(ltermToken,categoryCode) {
			var urlEndCode = encodeURIComponent(ltermToken)+'/'+encodeURIComponent(categoryCode);

			return this.pendingApprovalEndpoint.subRoute(urlEndCode).get().then(angular.bind(this, function(response) {
				if(response.status != WipsHttpStatusCodesConstant.statusTextAccepted){
					return response;
				}else{
					WcAlertConsoleService.addMessage({
						message: $translate.instant(response.errorMessage),
						type: 'danger',
						multiple: false
					});
					return response;
				}

			}), function(failure) {
				if(failure.status == WipsHttpStatusCodesConstant.conflict409){
					var param = {
							categoryCode: User.currentCategoryCode,
							ltermToken:User.userInformation.ltermToken
					};
					$state.go('atp-pending-approval',param,{reload: true});
					WcAlertConsoleService.addMessage({
						message:$translate.instant(failure.data.errorMessage),
						type: 'danger',
						multiple: false
					});
				}else{
					WipsHttpStatusCodesServices.getWipsHttpStatusCodes(failure)
				}

				return $q.reject(failure);
			});
		};
	}]);
