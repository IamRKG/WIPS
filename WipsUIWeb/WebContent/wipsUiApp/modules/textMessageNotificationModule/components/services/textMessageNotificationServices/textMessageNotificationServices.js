'use strict';
angular.module('WipsUiApp.textMessageNotificationModule')
	.service('textMessageNotificationServices', ['WcHttpEndpointPrototype','$q','WcHttpRequestService','WcAlertConsoleService','$translate','$state','User','WipsHttpStatusCodesConstant','WipsHttpStatusCodesServices',function(WcHttpEndpointPrototype,$q,WcHttpRequestService,WcAlertConsoleService,$translate,$state,User,WipsHttpStatusCodesConstant,WipsHttpStatusCodesServices) {
		
		this.textMessageNotificationSuccess = new WcHttpEndpointPrototype('textMessage/retriveDetails');
		this.textMessageNotificationSelected = new WcHttpEndpointPrototype('textMessage/saveSelectedOption');
		this.getTextMessageNotificationResponse = function(ltermToken) {
		var urlEndCode = encodeURIComponent(ltermToken);
			return this.textMessageNotificationSuccess.subRoute(urlEndCode).get().then(angular.bind(this, function(response) {
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
				WipsHttpStatusCodesServices.getWipsHttpStatusCodes(failure)
				return $q.reject(failure);
			});
		};
		this.getTextMessageSelectedResponse = function(param) {
			var urlEndCode = '';
				return this.textMessageNotificationSelected.put(urlEndCode,param).then(angular.bind(this, function(response) {
					if(response.status != WipsHttpStatusCodesConstant.statusTextAccepted){
						WcAlertConsoleService.addMessage({
							message:$translate.instant('application.success.Message'),
							type: 'success',
							multiple: false
						});
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
					WipsHttpStatusCodesServices.getWipsHttpStatusCodes(failure)
					return $q.reject(failure);
				});
			};

	}]);
