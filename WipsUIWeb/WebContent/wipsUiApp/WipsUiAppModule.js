'use strict';

/*
 * Define application module
 */
angular.module('WipsUiAppModule', [
	'WebCoreModule',
	'WipsUiApp.ComponentsModule',
	'WipsUiApp.LoginModule',
	'WipsUiApp.HomePendingApprovalModule',
	'WipsUiApp.ATPPendingApprovalModule',
	'WipsUiApp.LumpSumPendingApprovalModule',
	'WipsUiApp.priceClaimPendingApprovalModule',
	'WipsUiApp.textMessageNotificationModule'
]);

/*
 * Application module configuration
 */
angular.module('WipsUiAppModule')
.config(['WcTranslateConfiguratorServiceProvider', '$urlRouterProvider', '$stateProvider',
	function(WcTranslateConfiguratorServiceProvider, $urlRouterProvider, $stateProvider) {

		$stateProvider.state('wips-ui-app', {
			'abstract': true,
			url: '',
			templateUrl: 'wipsUiApp/wipsUiAppTemplate.html',
			controller: 'WipsUiAppController',
			controllerAs: 'wipsUiAppController'
		});

		WcTranslateConfiguratorServiceProvider.configureTranslateService();

		$urlRouterProvider.otherwise('/');
	}
])
.run(['WcHttpRequestService', '$window', 'Constants', 'WcAlertConsoleService', 'WcTranslateConfiguratorService', '$rootScope',
      function(WcHttpRequestService, $window, Constants, WcAlertConsoleService, WcTranslateConfiguratorService, $rootScope) {

   
   $rootScope.$on('$stateChangeError',function(event, toState, toParams, fromState, fromParams, error){ 
      
      //TODO: home page(ATP Pending approval) refresh action not working. due to no services for home page.
      console.log(error);
      
      
   });

   $rootScope.$on("$stateChangeStart", function(event, toState, toParams, fromState, fromParams) {
	   var stateName=toState.name;
	   var checkState=stateName.includes("sms");
      if(checkState){
    	  pathUrl('/SmsWeb/SMS/');
   }else{
	   pathUrl('/WipsWeb/WIPS/REST/');
      }
   });

   //relative path required for support of all deployment environments.
   //for development purposes, this can be switched to identify the deployed environment you'd like to hit for rest calls
   //if you change this, put it back before checking in! the unit tests will fail to tell you you don't have a relative url, as well.


         //WcHttpRequestService.configureDefaults({baseUrl: '/WipsWeb/WIPS/REST/'});

   var pathUrl = function(pathUrlParam){
      WcHttpRequestService.configureDefaults({baseUrl: pathUrlParam});
   }
   WcTranslateConfiguratorService.loadPartAndRefresh('WipsUiApp');
   
   WcHttpRequestService.configuration.timeout = 300000;

}]);



