'use strict';
angular.module('WipsUiApp.priceClaimPendingApproval.ComponentsModule')
	.service('priceClaimPendingApprovalServices', ['WcHttpEndpointPrototype','$q','WcAlertConsoleService','$translate','$state','User','WipsHttpStatusCodesConstant','WipsHttpStatusCodesServices',function(WcHttpEndpointPrototype,$q,WcAlertConsoleService,$translate,$state,User,WipsHttpStatusCodesConstant,WipsHttpStatusCodesServices) {

		this.priceClaimPendingApproval = new WcHttpEndpointPrototype('PendingApprovals');
		
		/*TODO: As per new design - clean var and function name */
		this.getPriceClaimPendingApproval = function(ltermToken,categoryCode) {
			var urlEndCode = encodeURIComponent(ltermToken)+'/'+encodeURIComponent(categoryCode);
			
			return this.priceClaimPendingApproval.subRoute(urlEndCode).get().then(angular.bind(this, function(response) {
				if(response.status != WipsHttpStatusCodesConstant.statusTextAccepted){
					return $q.when(response);
				}else{
					WcAlertConsoleService.addMessage({
						message: $translate.instant(response.errorMessage),
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
					$state.go('price-claim-pending-approval',param,{reload: true});
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
