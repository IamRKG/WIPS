'use strict';

describe('WipsUiApp.ComponentsModule UserService:',function(){
	
	var UserService, WcHttpEndpointPrototype, $rootScope;
	
	beforeEach(function() {
		// Module & Providers
		module('WipsUiApp.ComponentsModule');
		
		inject(function($injector) {
			UserService = $injector.get('UserService');
			WcHttpEndpointPrototype = $injector.get('WcHttpEndpointPrototype');
			$rootScope = $injector.get('$rootScope');
		});
	});
	
	it('establishes a restangular endpoint for user information', function() {
		expect(UserService.userInformationEndpoint.route).toEqual('WipsLogin/UserAuth');
	});
	
})