'use strict';
angular.module('WipsUiApp.LumpSumPendingApproval.ComponentsModule')
	.service('LumpSumDetailsApprovalServices', ['WcHttpEndpointPrototype','$q', '$translate', 'WcAlertConsoleService','$state','User','WipsHttpStatusCodesConstant','WipsHttpStatusCodesServices',function(WcHttpEndpointPrototype,$q,$translate,WcAlertConsoleService,$state,User,WipsHttpStatusCodesConstant,WipsHttpStatusCodesServices) {

		this.LumpSumDetailsApproval = new WcHttpEndpointPrototype('ViewLumpSumDetails/retrieveLumpSumDetails');
		this.LumpSumDetailsApprovalConfirm = new WcHttpEndpointPrototype('ViewLumpSumDetails/ConfirmPaymentDesc');
		this.LumpSumDetailsApprovalSavedDetails = new WcHttpEndpointPrototype('ViewLumpSumDetails/saveLumpSumDetails');
		this.SaveAndApproveOrRejectLumpSumDetails = new WcHttpEndpointPrototype('ViewLumpSumDetails/SaveAndApproveOrRejectLumpSumDetails');
		/*TODO: As per new design - clean var and function name */
		this.getLumpSumDetailsApproval = function(ltermToken,lumpsumNumber,defaultAmendment) {
			var urlEndCode = encodeURIComponent(ltermToken)+'/'+encodeURIComponent(lumpsumNumber)+'/'+encodeURIComponent(defaultAmendment);
			
			return this.LumpSumDetailsApproval.subRoute(urlEndCode).get().then(angular.bind(this, function(response) {
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
					$state.go('lump-sum-pending-approval',param,{reload: true});
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
		this.getConfirm = function(param) {
			var urlEndCode = "";
			return this.LumpSumDetailsApprovalConfirm.put(urlEndCode,param).then(angular.bind(this, function(response) {
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
				if(failure.status == '401'){
					$state.go('login.enter-login-information');
					User.userInformation = undefined;
					WcAlertConsoleService.addMessage({
						message:$translate.instant('sessionTimeOut.message'),
						type: 'warning',
						multiple: false
					});
					
				}else if(failure.status == '409'){
				var param = {
						categoryCode: User.currentCategoryCode,
						ltermToken:User.userInformation.ltermToken
				};
				
				$state.go('lump-sum-pending-approval',param);
				WcAlertConsoleService.addMessage({
					message:failure.data.errorMessage,
					type: 'danger',
					multiple: false
				});
			}
				else if(failure.status == '500'){
					WcAlertConsoleService.addMessage({
						message:$translate.instant('application.errors.internalServerError'),
						type: 'danger',
						multiple: false
					});
				}
				else{
					if(failure.data.warningFor4499 == true){
						
						WcAlertConsoleService.addMessage({
							message:failure.data.errorMessage,
							type: 'warning',
							multiple: false
						});
						
					}else{
						
						WcAlertConsoleService.addMessage({
							message:failure.data.errorMessage,
							type: 'danger',
							multiple: false
						});
						
					}

				};
				return $q.reject(failure);
			});
		};
		this.getSavedDetails = function(paramSave) {
			var urlEndCode = "";
			return this.LumpSumDetailsApprovalSavedDetails.put(urlEndCode,paramSave).then(angular.bind(this, function(response) {
				if(response.status != WipsHttpStatusCodesConstant.statusTextAccepted){
					return $q.when(response.data);
				}else{
					WcAlertConsoleService.addMessage({
						message: $translate.instant(response.data.errorMessage),
						type: 'danger',
						multiple: false
					});
					return $q.when(response.data);
				}
			}), function(failure) {
				if(failure.status == '401'){
					$state.go('login.enter-login-information');
					User.userInformation = undefined;
					WcAlertConsoleService.addMessage({
						message:$translate.instant('sessionTimeOut.message'),
						type: 'warning',
						multiple: false
					});
					
				}else if(failure.status == '500'){
					WcAlertConsoleService.addMessage({
						message:$translate.instant('application.errors.internalServerError'),
						type: 'danger',
						multiple: false
					});
				}
				else if(failure.status == '409'){
					var param = {
							categoryCode: User.currentCategoryCode,
							ltermToken:User.userInformation.ltermToken
					};
					$state.go('lump-sum-pending-approval',param);
					WcAlertConsoleService.addMessage({
						message:failure.data.errorMessage,
						type: 'danger',
						multiple: false
					});
				}
				else{
					if(failure.data.warningFor4499 == true){
						
						WcAlertConsoleService.addMessage({
							message:failure.data.errorMessage,
							type: 'warning',
							multiple: false
						});
						
					}else{
						
						WcAlertConsoleService.addMessage({
							message:failure.data.errorMessage,
							type: 'danger',
							multiple: false
						});
						
					}

				};
				return $q.reject(failure);
			});
		};
		this.saveAndApproveReject = function(param) {
			var urlEndCode = "";
			return this.SaveAndApproveOrRejectLumpSumDetails.put(urlEndCode,param).then(angular.bind(this, function(response) {
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
				if(failure.status == '401'){
					$state.go('login.enter-login-information');
					User.userInformation = undefined;
					WcAlertConsoleService.addMessage({
						message:$translate.instant('sessionTimeOut.message'),
						type: 'warning',
						multiple: false
					});
					
				}else if(failure.status == '500'){
					WcAlertConsoleService.addMessage({
						message:$translate.instant('application.errors.internalServerError'),
						type: 'danger',
						multiple: false
					});
				}
				else if(failure.status == '409'){
					var param = {
							categoryCode: User.currentCategoryCode,
							ltermToken:User.userInformation.ltermToken
					};
					$state.go('lump-sum-pending-approval',param);
					WcAlertConsoleService.addMessage({
						message:failure.data.errorMessage,
						type: 'danger',
						multiple: false
					});
				}
				else{
					if(failure.data.warningFor4499 == true){
						
						WcAlertConsoleService.addMessage({
							message:failure.data.errorMessage,
							type: 'warning',
							multiple: false
						});
						
					}else{
						
						WcAlertConsoleService.addMessage({
							message:failure.data.errorMessage,
							type: 'danger',
							multiple: false
						});
						
					}

				};
				return $q.reject(failure);
			});
		};

	}]);
