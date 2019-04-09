'use strict';
angular.module('WipsUiApp.ATPPendingApprovalModule', [
	'WebCoreModule',
	'WipsUiApp.ATPPendingApproval.ComponentsModule'
]);


angular.module('WipsUiApp.ATPPendingApprovalModule')
	.config(['$urlRouterProvider', '$stateProvider',
		function($urlRouterProvider, $stateProvider) {

			$urlRouterProvider
			.when('', '/login')
			.when('/', '/login');

			$stateProvider
				.state('atp-pending-approval', {
					url:'/atp-pending-approval/:categoryCode/:ltermToken',
					templateUrl: 'wipsUiApp/modules/homePendingApprovalModule/atpPendingApprovalModule/states/atpPendingApproval/ATPPendingApprovalTemplate.html',
					controller: 'ATPPendingApprovalController',
					controllerAs: 'atpPendingApprovalController',
					parent: 'wips-ui-app',
					resolve: {
						resolvedATPApproval: ['$stateParams', 'ATPPendingApprovalServices','User',function($stateParams, ATPPendingApprovalServices,User) {
							if(this.data.clickedBackButtonCallNewData == ''){
									return ATPPendingApprovalServices.getATPPendingApproval($stateParams.ltermToken,$stateParams.categoryCode).then(angular.bind(this,function(atpApproval){
									return atpApproval;
								}));
							}else{
								return undefined;
							}
						}]
					},
					data:{
						clickedBackButtonCallNewData:''
					}

				}).state('atp-recap', {
				//TODO: As per new design refactor altJobCode param. example like remarks
					url:'/atp-recap/:ltermToken/:atpNumber',
					templateUrl: 'wipsUiApp/modules/homePendingApprovalModule/atpPendingApprovalModule/states/atpRecap/ATPRecapTemplate.html',
					controller: 'ATPRecapController',
					controllerAs: 'atpRecapController',
					parent: 'wips-ui-app',
					resolve: {
						resolvedATPRecap: ['$stateParams', 'ATPRecapServices','User',function($stateParams, ATPRecapServices,User) {
							var ltermToken = User.userInformation.ltermToken;
							return ATPRecapServices.getATPRecap(ltermToken,$stateParams.atpNumber);
						}]
					}
				}).state('atp-remarks', {
					url:'/atp-remarks/:ltermToken/:atpNumber',
					templateUrl: 'wipsUiApp/modules/homePendingApprovalModule/atpPendingApprovalModule/states/atpRemarks/ATPRemarksTemplate.html',
					controller: 'ATPRemarksController',
					controllerAs: 'atpRemarksController',
						resolve: {
						resolvedATPRemarks: ['$stateParams', 'ATPRemarksServices','User',function($stateParams, ATPRemarksServices ,User) {
							var ltermToken = User.userInformation.ltermToken;
							return ATPRemarksServices.getATPRemarks(ltermToken,$stateParams.atpNumber);
						}]
					},
					parent: 'wips-ui-app'
				}).state('atp-strategy-details',{
					url:'/atp-strategy-details/:ltermToken/:atpNumber',
					templateUrl: 'wipsUiApp/modules/homePendingApprovalModule/atpPendingApprovalModule/states/atpStrategyDetails/ATPStrategyDetailsTemplate.html',
					controller: 'ATPStrategyDetailsController',
					controllerAs: 'atpStrategyDetailsController',
					parent: 'wips-ui-app',
					resolve: {
						resolvedATPStrategyDetails: ['$stateParams', 'ATPStrategyDetailsServices','User',function($stateParams, ATPStrategyDetailsServices ,User) {
							var ltermToken = User.userInformation.ltermToken;
							return ATPStrategyDetailsServices.getATPStrategyDetails(ltermToken,$stateParams.atpNumber);
						}]
					},
					
				});
		}
	]);
