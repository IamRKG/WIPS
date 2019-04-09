'use strict';

describe('WebCoreModule WcAuthorizationCacheHelper:', function() {

	var WcAuthorizationCacheHelper, $rootScope, $q;
	var policyName = 'CreateBooking:execute';
	var policy = {
		'resource': 'CreateBooking',
		'action': 'execute',
		'authorized': true
	};
	var policyPromise;
	var formattedPolicy = {
		'policy': 'CreateBooking:execute',
		'authorized': true
	};

	beforeEach(function() {
		module('WebCoreModule', function() {});

		inject(function($injector) {
			WcAuthorizationCacheHelper = $injector.get('WcAuthorizationCacheHelper');
			$q = $injector.get('$q');
			$rootScope = $injector.get('$rootScope');
		});

		policyPromise = $q.when(policy);

		window.sessionStorage.clear();
		WcAuthorizationCacheHelper.cache = [];
	});

	it('should be defined', function() {
		expect(WcAuthorizationCacheHelper).toBeDefined();
	});

	describe('addPolicy()', function() {
		it('should detect and store a promise for a given policy', function() {
			expect(WcAuthorizationCacheHelper.cache).toEqual([]);
			WcAuthorizationCacheHelper.addPolicy(policyName, policyPromise);

			expect(WcAuthorizationCacheHelper.cache[0].policy).toEqual(policyName);
			expect(WcAuthorizationCacheHelper.cache[0].promise).toEqual(policyPromise);

			$rootScope.$apply();

			expect(WcAuthorizationCacheHelper.cache[0]).toEqual(formattedPolicy);
		});
		it('should store a policy in the cache after formatting it for storage', function() {
			spyOn(WcAuthorizationCacheHelper, 'formatPolicy').and.callThrough();
			expect(WcAuthorizationCacheHelper.cache).toEqual([]);

			WcAuthorizationCacheHelper.addPolicy(policyName, policyPromise);

			$rootScope.$apply();

			expect(WcAuthorizationCacheHelper.formatPolicy.calls.count()).toEqual(2);
			expect(WcAuthorizationCacheHelper.cache).toEqual([formattedPolicy]);
		});
	});

	describe('resetCache()', function() {
		it('should reassign cache variable to an empty array', function() {
			WcAuthorizationCacheHelper.addPolicy(policyName, policyPromise);

			WcAuthorizationCacheHelper.resetCache();

			expect(WcAuthorizationCacheHelper.cache).toEqual([]);
		});
	});

	describe('retrievePolicyAuthorization()', function() {
		it('should return a promise for the policy authorization status that is already cached', function() {
			WcAuthorizationCacheHelper.cache = [];
			WcAuthorizationCacheHelper.addPolicy(policyName, policyPromise);

			$rootScope.$apply();

			var isAuth = WcAuthorizationCacheHelper.retrievePolicyAuthorization('CreateBooking:execute');

			expect(isAuth.then).toBeDefined();

			var resolvedIsAuth;
			isAuth.then(function(value) {
				resolvedIsAuth = value;
			});

			$rootScope.$apply();

			expect(resolvedIsAuth).toEqual(true);
		});
		it('should return a promise for the policy authorization status that is pending', function() {
			WcAuthorizationCacheHelper.cache = [];
			WcAuthorizationCacheHelper.addPolicy(policyName, policyPromise);

			var isAuth = WcAuthorizationCacheHelper.retrievePolicyAuthorization('CreateBooking:execute');

			$rootScope.$apply();

			expect(isAuth.then).toBeDefined();

			var resolvedIsAuth;
			isAuth.then(function(value) {
				resolvedIsAuth = value;
			});

			$rootScope.$apply();

			expect(resolvedIsAuth).toEqual(true);
		});
		it('should return null for a requested policy that is not already cached', function() {
			WcAuthorizationCacheHelper.cache = [];

			var isAuth = WcAuthorizationCacheHelper.retrievePolicyAuthorization(formattedPolicy);

			expect(isAuth).toBe(null);
		});

	});

});