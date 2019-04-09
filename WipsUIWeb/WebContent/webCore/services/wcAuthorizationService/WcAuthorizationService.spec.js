'use strict';

describe('WebCoreModule WcAuthorizationService:', function() {

	var WcAuthorizationService, WcAuthorizationCacheHelper, $q, $rootScope, Constants, $state;
	var formattedPolicyString = 'CreateBooking:execute';
	var formattedPolicyArray = ['CreateBooking:execute', 'DeleteBooking:execute'];
	var formattedPolicies = ['CreateBooking:execute', 'DeleteBooking:execute'];
	var policyObject1 = {
		'resource': 'CreateBooking',
		'action': 'execute',
		'authorized': true
	};
	var policyObject2 = {
		'resource': 'DeleteBooking',
		'action': 'execute',
		'authorized': false
	};
	var mockObject = [{
		'resource': 'CreateBooking',
		'action': 'execute',
		'authorized': true
	}];

	var policyName1 = 'CreateBooking:execute';
	var policyName2 = 'DeleteBooking:execute';

	var policyPromise1;
	var policyPromise2;
	var MockPromise;


	beforeEach(function() {
		module('WebCoreModule');

		inject(function($injector) {
			WcAuthorizationService = $injector.get('WcAuthorizationService');
			WcAuthorizationCacheHelper = $injector.get('WcAuthorizationCacheHelper');
			$q = $injector.get('$q');
			$rootScope = $injector.get('$rootScope');
			Constants = $injector.get('Constants');
			$state = $injector.get('$state');
		});

		policyPromise1 = $q.when(policyObject1);
		policyPromise2 = $q.when(policyObject2);
		MockPromise = $q.when(mockObject);

		WcAuthorizationCacheHelper.addPolicy(policyName1, policyPromise1);
		WcAuthorizationCacheHelper.addPolicy(policyName2, policyPromise2);

		//window.sessionStorage.role = angular.toJson('agent');
		$rootScope.$apply();
	});

	it('should be defined', function() {
		expect(WcAuthorizationService).toBeDefined();
	});

	describe('isAuthorized()', function() {
		it('should return true for a given policy set and authorization criteria when the user has privilige', function() {
			var isAuth;
			WcAuthorizationService.isAuthorized(formattedPolicyArray, Constants.authorization.any).then(function(auth) {
				isAuth = auth;
			});

			$rootScope.$apply();

			expect(isAuth).toEqual(true);
		});
		it('should iterate over each requested policy and obtain authorization status', function() {
			spyOn(WcAuthorizationService, 'isAuthorizedFromCache').and.callThrough();

			WcAuthorizationService.isAuthorized(formattedPolicyArray, Constants.authorization.any);

			expect(WcAuthorizationService.isAuthorizedFromCache.calls.count()).toEqual(2);
		});
		it('should, given any, check the policy cache return true if the aggregate conditional OR of their values', function() {
			spyOn(WcAuthorizationService, 'isAuthorizedFromCache').and.callThrough();

			var isAuth;

			WcAuthorizationService.isAuthorized(formattedPolicyArray, Constants.authorization.any).then(function(auth) {
				isAuth = auth;
			});

			$rootScope.$apply();

			expect(WcAuthorizationService.isAuthorizedFromCache).toHaveBeenCalled();
			expect(isAuth).toEqual(true);
		});
		it('should, given all, check the policy cache return true if the aggregate conditional AND of their values', function() {
			spyOn(WcAuthorizationService, 'isAuthorizedFromCache').and.callThrough();

			var isAuth;
			WcAuthorizationService.isAuthorized(formattedPolicies, Constants.authorization.all).then(function(auth) {
				isAuth = auth;
			});

			$rootScope.$apply();

			expect(WcAuthorizationService.isAuthorizedFromCache).toHaveBeenCalled();
			expect(isAuth).toEqual(false);
		});
		it('should request policy that is not already cached', function() {
			WcAuthorizationCacheHelper.resetCache();
			spyOn(WcAuthorizationService, 'requestAuthorization').and.callThrough();
			spyOn(WcAuthorizationService.authorizationEndpoint, 'get').and.returnValue(MockPromise);

			var isAuth;
			WcAuthorizationService.isAuthorized(formattedPolicyString, Constants.authorization.all).then(function(auth) {
				isAuth = auth;
			});

			$rootScope.$apply();

			expect(WcAuthorizationService.requestAuthorization.calls.count()).toEqual(1);
			expect(isAuth).toEqual(true);
		});
		it('should request missing policies and add them to the cache', function() {
			spyOn(WcAuthorizationService, 'requestAuthorization').and.callThrough();
			spyOn(WcAuthorizationService.authorizationEndpoint, 'get').and.callFake(function() {
				return $q.when([{
					'resource': 'ManageAgent',
					'action': 'execute',
					'authorized': false
				}]);
			});

			var isAuth;
			WcAuthorizationService.isAuthorized(['CreateBooking:execute', 'DeleteBooking:execute', 'ManageAgent:execute'], Constants.authorization.all).then(function(auth) {
				isAuth = auth;
			});

			$rootScope.$apply();

			expect(WcAuthorizationService.requestAuthorization.calls.count()).toEqual(1);
			expect(isAuth).toEqual(false);
		});
	});

	describe('isStateAuthorized():', function() {

		it('should return true for a protected state which user is authorized to view based on applicable policies from state definition', function() {
			var protectedState = 'create-booking';
			spyOn(WcAuthorizationService, 'isAuthorized').and.returnValue(true);
			spyOn($state, 'get').and.callFake(function() {
				return {
					data: {
						policies: ['CreateBooking:execute']
					}
				};
			});

			var isAuth = WcAuthorizationService.isStateAuthorized(protectedState);

			expect(WcAuthorizationService.isAuthorized).toHaveBeenCalledWith(['CreateBooking:execute'], 'All');
			expect(isAuth).toBeTruthy();
		});

		it('should return true for an unprotected state', function() {
			var unprotectedState = 'list-booking';
			spyOn(WcAuthorizationService, 'isAuthorized');
			spyOn($state, 'get').and.callFake(function() {
				return {
					data: {}
				};
			});

			var isAuth = WcAuthorizationService.isStateAuthorized(unprotectedState);

			expect(WcAuthorizationService.isAuthorized).not.toHaveBeenCalled();
			expect(isAuth).toBeTruthy();
		});
	});

	describe('requestAuthorization()', function() {
		it('should update policy cache with authorization promise from the server', function() {
			spyOn(WcAuthorizationCacheHelper, 'addPolicy');
			spyOn(WcAuthorizationService.authorizationEndpoint, 'get').and.returnValue(policyPromise1);


			WcAuthorizationService.requestAuthorization(formattedPolicyString);
			$rootScope.$apply();

			expect(WcAuthorizationCacheHelper.addPolicy).toHaveBeenCalled();
			expect(WcAuthorizationService.authorizationEndpoint.get).toHaveBeenCalled();
		});
	});

	describe('isAuthorizedFromCache()', function() {
		it('should return promised authorization status for a cached policy', function() {
			var isAuth;
			WcAuthorizationService.isAuthorizedFromCache(formattedPolicyString).then(function(auth) {
				isAuth = auth;
			});

			$rootScope.$apply();

			expect(isAuth).toEqual(true);
		});
	});

});