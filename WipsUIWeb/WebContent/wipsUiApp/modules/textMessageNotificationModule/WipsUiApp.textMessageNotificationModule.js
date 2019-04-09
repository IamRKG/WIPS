'use strict';
angular.module('WipsUiApp.textMessageNotificationModule', [
	'WebCoreModule',
	'WipsUiApp.textMessageNotification.ComponentsModule'
]);

angular.module('WipsUiApp.textMessageNotificationModule')
	.config(['$urlRouterProvider', '$stateProvider',
		function($urlRouterProvider, $stateProvider) {
			$urlRouterProvider
			.when('', '/login')
			.when('/', '/login');
			$stateProvider
				.state('home-textMessageNotification', {
					url:'/home-textMessageNotification',
					templateUrl: 'wipsUiApp/modules/textMessageNotificationModule/states/textMessageNotificationTemplate.html',
					controller: 'textMessageNotificationController',
					controllerAs: 'textMessageNotificationController',
					resolve: {
						resolvedTextMessageNotificationData: ['User', 'textMessageNotificationServices', function(User, textMessageNotificationServices) {
							
							return textMessageNotificationServices.getTextMessageNotificationResponse(User.userInformation.ltermToken).then(angular.bind(this,function(textMessageDatas){
								return textMessageDatas;
							}));
					
						}]
					},
					parent: 'wips-ui-app'
				});
			}
	
	]);

	

