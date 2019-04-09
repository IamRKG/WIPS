'use strict';
angular.module('WipsUiApp.LumpSumPendingApprovalModule', [
	'WebCoreModule',
	'WipsUiApp.LumpSumPendingApproval.ComponentsModule'
]);


angular.module('WipsUiApp.LumpSumPendingApprovalModule')
	.config(['$urlRouterProvider', '$stateProvider',
		function($urlRouterProvider, $stateProvider) {

			$urlRouterProvider
			.when('', '/login')
			.when('/', '/login');

			$stateProvider
				.state('lump-sum-pending-approval', {
					url:'/lump-sum-pending-approval/:categoryCode/:ltermToken',
					templateUrl: 'wipsUiApp/modules/homePendingApprovalModule/lumpSumPendingApprovalModule/states/lumpSumPendingApproval/LumpSumPendingApprovalTemplate.html',
					controller: 'LumpSumPendingApprovalController',
					controllerAs: 'lumpSumPendingApprovalController',
					resolve: {
						resolvedLumpSumApprovalList: ['$stateParams', 'LumpSumPendingApprovalServices', function($stateParams, LumpSumPendingApprovalServices) {
							if(this.data.clickedBackButtonCallNewData == ''){
							return LumpSumPendingApprovalServices.getLumpSumPendingApproval($stateParams.ltermToken,$stateParams.categoryCode);
							}
							else{
								return undefined;
							}
						}]
					},
					data:{
						clickedBackButtonCallNewData:''
					},
					parent: 'wips-ui-app'
			}).state('lump-sum-details-approvals', {
				url:'/lump-sum-details-approvals/:ltermToken/:lumpsumNumber/:defaultAmendment',
				templateUrl: 'wipsUiApp/modules/homePendingApprovalModule/lumpSumPendingApprovalModule/states/lumpSumDetailsApprovals/LumpSumDetailsApprovalsTemplate.html',
				controller: 'LumpSumDetailsApprovalsController',
				controllerAs: 'lumpSumDetailsApprovalsController',
				resolve: {
					resolvedLumpSumApproval: ['$stateParams', 'LumpSumDetailsApprovalServices','User',function($stateParams, LumpSumDetailsApprovalServices,User) {
							return LumpSumDetailsApprovalServices.getLumpSumDetailsApproval($stateParams.ltermToken,$stateParams.lumpsumNumber,$stateParams.defaultAmendment).then(angular.bind(this,function(lumpsumApproval){
							return lumpsumApproval;
						}));
					}]
				},
				parent: 'wips-ui-app'
			});
		}]);
