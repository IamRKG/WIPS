angular.module('WipsUiApp.ATPPendingApproval.ComponentsModule')
	.service('ATPApproveServices', ['WcHttpEndpointPrototype','$q','WcAlertConsoleService', '$translate','SessionServices','$state','User','WipsHttpStatusCodesConstant','WipsHttpStatusCodesServices',function(WcHttpEndpointPrototype,$q,WcAlertConsoleService, $translate, SessionServices,$state,User,WipsHttpStatusCodesConstant,WipsHttpStatusCodesServices) {

		this.atpApproveEndpoint = new WcHttpEndpointPrototype('AtpApproval/ApproveOrReject');
		this.SaveRemarksAndApproveOrRejectAtpEndpoint = new WcHttpEndpointPrototype('AtpApproval/SaveRemarksAndApproveOrRejectAtp');
		
		this.getATPApprove = function(param) {
			var urlEndCode = "";
			return this.atpApproveEndpoint.put(urlEndCode,param).then(angular.bind(this, function(response) {
				if(response.status != WipsHttpStatusCodesConstant.statusTextAccepted){
					return $q.when(response.data);
				}else{
					WcAlertConsoleService.addMessage({
						message: $translate.instant(response.data.errorMessage),
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

		this.getSaveRemarksAndApproveOrRejectAtp = function(param) {
			var urlEndCode = "";
			return this.SaveRemarksAndApproveOrRejectAtpEndpoint.put(urlEndCode,param).then(angular.bind(this, function(response) {
				if(response.status != WipsHttpStatusCodesConstant.statusTextAccepted){
					return $q.when(response.data);
				}else{
					WcAlertConsoleService.addMessage({
						message: $translate.instant(response.data.errorMessage),
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