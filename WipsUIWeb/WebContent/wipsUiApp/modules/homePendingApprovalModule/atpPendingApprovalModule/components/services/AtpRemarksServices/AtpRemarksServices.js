angular.module('WipsUiApp.ATPPendingApproval.ComponentsModule')
	.service('ATPRemarksServices', ['WcHttpEndpointPrototype','$q','AtpRemarksPrototype','WcAlertConsoleService', '$translate', 'SessionServices','$state','User','WipsHttpStatusCodesConstant',function(WcHttpEndpointPrototype,$q,AtpRemarksPrototype,WcAlertConsoleService, $translate, SessionServices,$state,User,WipsHttpStatusCodesConstant) {

		this.atpRemarksEndpoint = new WcHttpEndpointPrototype('AtpApproval/ATPRemarks');
		this.saveAtpRemarksEndpoint = new WcHttpEndpointPrototype('AtpApproval/SaveATPRemarks');

		this.getATPRemarks = function(ltermToken,atpNumber) {
			var urlEndCode = encodeURIComponent(ltermToken)+'/'+encodeURIComponent(atpNumber);
			
			return this.atpRemarksEndpoint.subRoute(urlEndCode).get().then(angular.bind(this, function(response) {
				response = new AtpRemarksPrototype(response);

				if(response.status != WipsHttpStatusCodesConstant.statusTextAccepted){
					return response;
				}else{
					WcAlertConsoleService.addMessage({
						message: $translate.instant(response.errorMessage),
						type: 'danger',
						multiple: false
					});
					return $q.reject(response); /*TODO:Test without $q.reject*/
				}
			}), function(failure) {
				if(failure.status == '401'){
					$state.go('login.enter-login-information');
					User.getClearUserInformation();
					WcAlertConsoleService.addMessage({
						message:$translate.instant('sessionTimeOut.message'),
						type: 'warning',
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
				else if(failure.status == '409'){
					var param = {
							categoryCode: User.currentCategoryCode,
							ltermToken:User.userInformation.ltermToken
					};
					$state.go('atp-pending-approval',param);
					WcAlertConsoleService.addMessage({
						message:failure.data.errorMessage,
						type: 'danger',
						multiple: false
					});
				}else{
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
		
		
		this.getSaveRemarks = function(param) {
			var urlEndCode = "";
			return this.saveAtpRemarksEndpoint.put(urlEndCode,param).then(angular.bind(this, function(response) {
				if(response.status != WipsHttpStatusCodesConstant.statusTextAccepted){
					return $q.when(response.data);
				}else{
					WcAlertConsoleService.addMessage({
						message: $translate.instant(response.data.errorMessage),
						type: 'danger',
						multiple: false
					});
					return $q.reject(response); /*TODO:Test without $q.reject*/
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
					
				}
				else if(failure.status == '500'){
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
					$state.go('atp-pending-approval',param);
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
