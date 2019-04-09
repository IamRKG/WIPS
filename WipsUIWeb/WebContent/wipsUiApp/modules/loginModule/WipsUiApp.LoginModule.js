'use strict';

/*
 * Define login module
 */
angular.module('WipsUiApp.LoginModule', [
	'WebCoreModule',
	'WipsUiApp.Login.ComponentsModule'
]);

/*
 * Agent login configuration
 */
angular.module('WipsUiApp.LoginModule')
	.config(['$urlRouterProvider', '$stateProvider',
		function($urlRouterProvider, $stateProvider) {

			$urlRouterProvider
				.when('', '/login')
				.when('/', '/login');

			$stateProvider
				.state('login', {
					'abstract': true,
					templateUrl: 'wipsUiApp/modules/loginModule/states/login/loginTemplate.html',
					controller: 'LoginController',
					controllerAs: 'loginController',
					parent: 'wips-ui-app'
				}).
				state('login.enter-login-information', {
					url: '/login',
					templateUrl: 'wipsUiApp/modules/loginModule/states/login/enterLoginInformation/enterLoginInformationTemplate.html',
					controller: 'EnterLoginInformationController',
					controllerAs: 'enterLoginInformationController',
					parent: 'login'
				});
		

		}
	]);
