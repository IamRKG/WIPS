'use strict';
angular.module('WipsUiApp.HomePendingApprovalModule', [
	'WebCoreModule',
	'WipsUiApp.HomePendingApproval.ComponentsModule'
]);


angular.module('WipsUiApp.HomePendingApprovalModule')
	.config(['$urlRouterProvider', '$stateProvider',
		function($urlRouterProvider, $stateProvider) {

			$urlRouterProvider
			.when('', '/login')
			.when('/', '/login');

			$stateProvider
				.state('home-atp-pending-approval', {
					url:'/home-atp-pending-approval',
					templateUrl: 'wipsUiApp/modules/homePendingApprovalModule/states/homePendingApproval/HomePendingApprovalTemplate.html',
					controller: 'HomePendingApprovalController',
					controllerAs: 'homePendingApprovalController',
					parent: 'wips-ui-app',
		
					resolve:{
						resolvedATPApproval:['User','SwitchJobServices','SwitchJobFactory','SessionServices',function(User,SwitchJobServices,SwitchJobFactory,SessionServices){
							if(User.userInformation == undefined){
							return undefined;
							}else{
							if(SessionServices.isMenuCliked == true || SwitchJobFactory.jobCode!=undefined){
								var ltermToken = User.userInformation.ltermToken;
								
								if(SwitchJobFactory.jobCode!=User.currentjobDetail.jobCode && SwitchJobFactory.jobCode!=undefined){
			                    var param={
									jobCode:SwitchJobFactory.jobCode,
								    jobName:SwitchJobFactory.jobName,
									ltermToken:ltermToken
			                    }
								}
								else{
									 var param={
								    jobCode:User.currentjobDetail.jobCode,
									jobName:User.currentjobDetail.jobName,
									ltermToken:ltermToken
									 }
									
								}
								if(this.data.clickedBackButtonCallNewData == ''){
								return 	SwitchJobServices.getJobCodeList(param).then(angular.bind(this, function(response) {
									return response;
								}));
								}
								}
								else{
									return undefined;
									};
								
							}
						}]
					},
					data:{
						clickedBackButtonCallNewData : ''
					}
				})
			}
	]);
