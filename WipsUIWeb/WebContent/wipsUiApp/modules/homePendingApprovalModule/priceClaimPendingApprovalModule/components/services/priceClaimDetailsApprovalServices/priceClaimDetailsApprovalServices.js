'use strict';
angular.module('WipsUiApp.priceClaimPendingApproval.ComponentsModule')
	.service('priceClaimDetailsApprovalServices', ['WcHttpEndpointPrototype','$q', '$translate', 'WcAlertConsoleService','$state','User','WipsHttpStatusCodesConstant','WipsHttpStatusCodesServices',function(WcHttpEndpointPrototype,$q,$translate,WcAlertConsoleService,$state,User,WipsHttpStatusCodesConstant,WipsHttpStatusCodesServices) {

		this.priceClaimDetailsApproval = new WcHttpEndpointPrototype('PriceClaims/retrievePriceClaimsDetails');
		this.priceClaimDetailsSavedDetails = new WcHttpEndpointPrototype('PriceClaims/saveClaimDetails');
		this.priceClaimDetailsApproveAndRejectDetails= new WcHttpEndpointPrototype('PriceClaims/approveClaimDetails');
		this.retrieveFinancialImpactDetails= new WcHttpEndpointPrototype('PriceClaims/retrieveFinancialImpactDetails');

		this.getPriceClaimDetailsApproval = function(ltermToken,priceClaimNumber,supplier) {
			var urlEndCode = encodeURIComponent(ltermToken)+'/'+encodeURIComponent(priceClaimNumber)+'/'+encodeURIComponent(supplier);
			
			return this.priceClaimDetailsApproval.subRoute(urlEndCode).get().then(angular.bind(this, function(response) {
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
		this.getSavedDetails = function(paramSave) {
			var urlEndCode = "";
			return this.priceClaimDetailsSavedDetails.put(urlEndCode,paramSave).then(angular.bind(this, function(response) {

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
		this.approveAndRejectDetails = function(param) {
			var urlEndCode = "";
			return this.priceClaimDetailsApproveAndRejectDetails.put(urlEndCode,param).then(angular.bind(this, function(response) {

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

		this.getReloadFinancialImpactInformation = function (param) {
			var urlEndCode = encodeURIComponent(param.ltermToken)+'/'+
				encodeURIComponent(param.priceClaimNumber)+'/'+
				encodeURIComponent(param.supplier)+'/'+
				encodeURIComponent(param.selectedPM)+'/'+
				encodeURIComponent(param.selectedYear);
			return this.retrieveFinancialImpactDetails.subRoute(urlEndCode).get().then(function (response) {
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

			},function(failure) {
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
			})}


	}]);
