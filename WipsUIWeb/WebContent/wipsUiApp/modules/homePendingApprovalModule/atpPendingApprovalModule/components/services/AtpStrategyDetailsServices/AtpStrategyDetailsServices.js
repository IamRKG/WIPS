angular.module('WipsUiApp.ATPPendingApproval.ComponentsModule')
	.service('ATPStrategyDetailsServices', ['WcHttpEndpointPrototype','$q', '$translate', 'AtpStrategyDetailsPrototype','WcAlertConsoleService','SessionServices','$state','User','WipsHttpStatusCodesConstant','WipsHttpStatusCodesServices',function(WcHttpEndpointPrototype,$q,$translate,AtpStrategyDetailsPrototype,WcAlertConsoleService,SessionServices,$state,User,WipsHttpStatusCodesConstant,WipsHttpStatusCodesServices) {

		this.atpStrategyDetailsEndpoint = new WcHttpEndpointPrototype('AtpApproval/Strategy');
		
		this.getATPStrategyDetails = function(ltermToken,atpNumber) {
			var urlEndCode = encodeURIComponent(ltermToken)+'/'+encodeURIComponent(atpNumber);
			
			return this.atpStrategyDetailsEndpoint.subRoute(urlEndCode).get().then(angular.bind(this, function(response) {
				response = new AtpStrategyDetailsPrototype(response);
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