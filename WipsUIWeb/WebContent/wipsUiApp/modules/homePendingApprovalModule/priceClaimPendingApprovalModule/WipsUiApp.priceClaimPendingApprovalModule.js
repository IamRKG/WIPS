'use strict';
angular.module('WipsUiApp.priceClaimPendingApprovalModule', [
	'WebCoreModule',
	'WipsUiApp.priceClaimPendingApproval.ComponentsModule'
]);


angular.module('WipsUiApp.priceClaimPendingApprovalModule')
	.config(['$urlRouterProvider', '$stateProvider',
		function($urlRouterProvider, $stateProvider) {

			$urlRouterProvider
			.when('', '/login')
			.when('/', '/login');

			$stateProvider
				.state('price-claim-pending-approval', {
					url:'/price-claim-pending-approval/:categoryCode/:ltermToken',
					templateUrl: 'wipsUiApp/modules/homePendingApprovalModule/priceClaimPendingApprovalModule/states/priceClaimPendingApproval/priceClaimPendingApprovalTemplate.html',
					controller: 'PriceClaimPendingApprovalController',
					controllerAs: 'priceClaimPendingApprovalController',
					resolve: {
						resolvedPriceClaimApprovalList: ['$stateParams', 'priceClaimPendingApprovalServices', function($stateParams, priceClaimPendingApprovalServices) {
							if(this.data.clickedBackButtonCallNewData == ''){
							return priceClaimPendingApprovalServices.getPriceClaimPendingApproval($stateParams.ltermToken,$stateParams.categoryCode);
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
			})
			.state('price-claim-details-approvals', {
				url:'/price-claim-details-approvals/:ltermToken/:priceClaimNumber/:supplier',
				templateUrl: 'wipsUiApp/modules/homePendingApprovalModule/priceClaimPendingApprovalModule/states/priceClaimDetailsApprovals/priceClaimDetailsApprovalsTemplate.html',
				controller: 'PriceClaimDetailsApprovalsController',
				controllerAs: 'priceClaimDetailsApprovalsController',
				resolve: {
					resolvedPriceClaimApproval: ['$stateParams', 'priceClaimDetailsApprovalServices','User',function($stateParams, priceClaimDetailsApprovalServices,User) {
							return priceClaimDetailsApprovalServices.getPriceClaimDetailsApproval($stateParams.ltermToken,$stateParams.priceClaimNumber,$stateParams.supplier).then(angular.bind(this,function(priceClaimApproval){
							return priceClaimApproval;
						}));
					}]
				},
				parent: 'wips-ui-app'
			});
		}]);
