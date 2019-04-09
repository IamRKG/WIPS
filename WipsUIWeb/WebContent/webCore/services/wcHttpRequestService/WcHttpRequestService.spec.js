'use strict';

describe('WebCoreModule WcHttpRequestService:', function() {

	var $q, $http, WcHttpRequestService, $httpBackend, $rootScope, $window;
	beforeEach(function() {
		// Module & Providers
		module('WebCoreModule');

		inject(function($injector) {
			WcHttpRequestService = $injector.get('WcHttpRequestService');
			$q = $injector.get('$q');
			$http = $injector.get('$http');
			$httpBackend = $injector.get('$httpBackend');
			$rootScope = $injector.get('$rootScope');
			$window = $injector.get('$window');
		});

		WcHttpRequestService.configureDefaults({baseUrl: 'http://www.ford.com/'});

		$httpBackend.whenGET('http://www.ford.com/test').respond(200, 'success!', null, 'success!');
		$httpBackend.whenGET('http://www.ford.com/testError').respond(500, 'fail!', null, 'fail!');

		$httpBackend.whenPOST('http://www.ford.com/test').respond(200, 'success!', null, 'success!');
		$httpBackend.whenPOST('http://www.ford.com/testError').respond(500, 'fail!', null, 'fail!');

		$httpBackend.whenPUT('http://www.ford.com/test').respond(200, 'success!', null, 'success!');
		$httpBackend.whenPUT('http://www.ford.com/testError').respond(500, 'fail!', null, 'fail!');

		$httpBackend.whenDELETE('http://www.ford.com/test').respond(200, 'success!', null, 'success!');
		$httpBackend.whenDELETE('http://www.ford.com/testError').respond(500, 'fail!', null, 'fail!');

		$httpBackend.whenGET('http://www.ford.com/unprotected/ping').respond(200, 'success!', null, 'success!');
		$httpBackend.whenGET('http://www.ford.com/user').respond(500, 'fail!', null, 'fail!');

		$window.localStorage.clear();
		$window.sessionStorage.clear();
	});

	it('defines a WcHttpRequestService', function() {
		expect(WcHttpRequestService).toBeDefined();
	});

	describe('configureDefaults():', function() {
		it('should define a configureDefaults() function', function() {
			spyOn(WcHttpRequestService, 'configureDefaults');

			expect(WcHttpRequestService.configureDefaults).toBeDefined();
		});

		it('should extend the configuration object with the passed in object', function() {
			spyOn(WcHttpRequestService, 'configureDefaults').and.callThrough();
			spyOn(angular, 'extend').and.callThrough();

			WcHttpRequestService.configureDefaults({unAuthenticatedCallback: null});
			expect(angular.extend).toHaveBeenCalledWith(WcHttpRequestService.configuration, {unAuthenticatedCallback: null});

			expect(WcHttpRequestService.configuration).toEqual({
				xsrfCookieName: 'WSL-credential',
				withCredentials: true,
				headers: {},
				timeout: 10000,
				baseUrl: 'http://www.ford.com/',
				cache: false,
				pingTrustPeriodMS: 1000,
				unAuthenticatedCallback: null
			});
		});
	});

	describe('getNetworkState():', function() {

		it('should initiate a new ping request and update state if there are no pending requests', function() {
			expect(WcHttpRequestService._pendingRequests).toEqual(0);
			spyOn($http, 'get').and.callFake(function() {
				return $q.when('online');
			});

			var promise = WcHttpRequestService.getNetworkState();

			expect($http.get).toHaveBeenCalled();
			expect(WcHttpRequestService._currentPingRequestPromise).toBe(promise);
			expect(WcHttpRequestService._pendingRequests).toEqual(1);
			expect(WcHttpRequestService._lastPingRequestTimestamp).not.toEqual(0);
			expect(WcHttpRequestService._trustedResponse).toEqual('');
		});

		it('should return the trusted response instead of issuing a new ping request if within the trusted duration', function() {
			expect(WcHttpRequestService._pendingRequests).toEqual(0);
			spyOn($http, 'get').and.callFake(function() {
				return $q.when('success');
			});

			WcHttpRequestService.getNetworkState();
			$rootScope.$apply();
			WcHttpRequestService.getNetworkState();

			expect($http.get.calls.count()).toEqual(1);
			expect(WcHttpRequestService._trustedResponse).toEqual('unauthenticated');

		});

		it('should return the saved ping promise if there are any pending ping requests', function() {
			var pendingPromise, secondRequestPromise;
			expect(WcHttpRequestService._pendingRequests).toEqual(0);
			pendingPromise = WcHttpRequestService.getNetworkState();
			expect(WcHttpRequestService._pendingRequests).toEqual(1);
			secondRequestPromise = WcHttpRequestService.getNetworkState();

			expect(WcHttpRequestService._currentPingRequestPromise).toBe(pendingPromise);
			expect(secondRequestPromise).toBe(WcHttpRequestService._currentPingRequestPromise);
		});

		it('should update state when ping request response is received', function() {
			expect(WcHttpRequestService._pendingRequests).toEqual(0);
			spyOn($http, 'get').and.callFake(function() {
				return $q.when('online');
			});

			var result;
			WcHttpRequestService.getNetworkState().then(function(actualResult) {
				result = actualResult;
			});

			$rootScope.$apply();

			expect(WcHttpRequestService._currentPingRequestPromise).toEqual({});
			expect(WcHttpRequestService._pendingRequests).toEqual(0);
			expect(WcHttpRequestService._trustedResponse).toEqual('unauthenticated');
		});

		it('should return unauthorized when online but not logged in', function() {
			expect(WcHttpRequestService._pendingRequests).toEqual(0);
			spyOn($http, 'get').and.callFake(function() {
				return $q.when('success');
			});

			var result;
			WcHttpRequestService.getNetworkState().then(function() {
			}, function(actualResult) {
				result = actualResult;
			});

			$rootScope.$apply();

			expect(WcHttpRequestService._currentPingRequestPromise).toEqual({});
			expect(WcHttpRequestService._pendingRequests).toEqual(0);
			expect(WcHttpRequestService._trustedResponse).toEqual('unauthenticated');
		});

		it('should return offline when offline', function() {
			expect(WcHttpRequestService._pendingRequests).toEqual(0);
			spyOn($http, 'get').and.callFake(function() {
				return $q.reject({status: 0});
			});

			var result;
			WcHttpRequestService.getNetworkState().then(function() {
			}, function(actualResult) {
				result = actualResult;
			});

			$rootScope.$apply();

			expect(WcHttpRequestService._currentPingRequestPromise).toEqual({});
			expect(WcHttpRequestService._pendingRequests).toEqual(0);
			expect(WcHttpRequestService._trustedResponse).toEqual('offline');
		});

	});

	describe('interceptors:', function() {

		it('should define an arrays for each interceptor type', function() {
			expect(angular.isArray(WcHttpRequestService.requestInterceptors)).toBeTruthy();
			expect(angular.isArray(WcHttpRequestService.responseInterceptors)).toBeTruthy();
			expect(angular.isArray(WcHttpRequestService.errorInterceptors)).toBeTruthy();
		});

		describe('addRequestInterceptor():', function() {
			it('should define an addInterceptor() function', function() {
				spyOn(WcHttpRequestService, 'addRequestInterceptor');

				expect(WcHttpRequestService.addRequestInterceptor).toBeDefined();
			});

			it('should push a request interceptor to the request interceptor\'s array', function() {
				expect(WcHttpRequestService.requestInterceptors.length).toEqual(0);

				var mockInterceptor = function() {
					return 'I\'m an interceptor!';
				};

				WcHttpRequestService.addRequestInterceptor(mockInterceptor);

				expect(WcHttpRequestService.requestInterceptors.length).toEqual(1);
			});
		});

		describe('triggerRequestInterceptors():', function() {
			it('should define a triggerRequestInterceptors() function', function() {
				spyOn(WcHttpRequestService, 'triggerRequestInterceptors');

				expect(WcHttpRequestService.triggerRequestInterceptors).toBeDefined();
			});

			it('should trigger each function in the request interceptor\'s array in index order', function() {
				expect(WcHttpRequestService.requestInterceptors.length).toEqual(0);

				var mockInterceptor1 = function(url, data, config) {
					return {url: 'newUrl'};
				};
				var mockInterceptor2 = function(url, data, config) {
					return {url: 'newUrl2'};
				};

				WcHttpRequestService.addRequestInterceptor(mockInterceptor1);
				WcHttpRequestService.addRequestInterceptor(mockInterceptor2);

				var interceptedParams = WcHttpRequestService.triggerRequestInterceptors('oldUrl', 'oldData', 'oldConfig');

				expect(interceptedParams).toEqual({url: 'newUrl2', data: 'oldData', config: 'oldConfig'});
			});

			it('should update the given parameters with new values when the interceptors return the changes', function() {
				expect(WcHttpRequestService.requestInterceptors.length).toEqual(0);

				var mockInterceptor1 = function(url, data, config) {
					return {url: 'newUrl'};
				};
				var mockInterceptor2 = function(url, data, config) {
					return {data: 'newData'};
				};
				var mockInterceptor3 = function(url, data, config) {
					return {config: 'newConfig'};
				};

				WcHttpRequestService.addRequestInterceptor(mockInterceptor1);
				WcHttpRequestService.addRequestInterceptor(mockInterceptor2);
				WcHttpRequestService.addRequestInterceptor(mockInterceptor3);

				var interceptedParams = WcHttpRequestService.triggerRequestInterceptors('oldUrl', 'oldData', 'oldConfig');

				expect(interceptedParams).toEqual({url: 'newUrl', data: 'newData', config: 'newConfig'});
			});

			it('should not modify the given parameters if none of the request interceptors modify the parameters', function() {
				expect(WcHttpRequestService.requestInterceptors.length).toEqual(0);

				var mockInterceptor1 = function(url, data, config) {};

				WcHttpRequestService.addRequestInterceptor(mockInterceptor1);

				var interceptedParams = WcHttpRequestService.triggerRequestInterceptors('oldUrl', 'oldData', 'oldConfig');

				expect(interceptedParams).toEqual({url: 'oldUrl', data: 'oldData', config: 'oldConfig'});
			});

			it('should not modify the given parameters if the request interceptors return data in an unrecognized format', function() {
				expect(WcHttpRequestService.requestInterceptors.length).toEqual(0);

				var mockInterceptor1 = function(url, data, config) {return {thisWillNotChange: true}};

				WcHttpRequestService.addRequestInterceptor(mockInterceptor1);

				var interceptedParams = WcHttpRequestService.triggerRequestInterceptors('oldUrl', 'oldData', 'oldConfig');

				expect(interceptedParams).toEqual({url: 'oldUrl', data: 'oldData', config: 'oldConfig'});
			});

		});

		describe('addResponseInterceptor():', function() {
			it('should define an addInterceptor() function', function() {
				spyOn(WcHttpRequestService, 'addResponseInterceptor');

				expect(WcHttpRequestService.addResponseInterceptor).toBeDefined();
			});

			it('should push a response interceptor to the response interceptor\'s array', function() {
				expect(WcHttpRequestService.responseInterceptors.length).toEqual(0);

				var mockInterceptor = function() {
					return 'I\'m an interceptor!';
				};

				WcHttpRequestService.addResponseInterceptor(mockInterceptor);

				expect(WcHttpRequestService.responseInterceptors.length).toEqual(1);
			});
		});

		describe('triggerResponseInterceptors():', function() {
			it('should define a triggerResponseInterceptors() function', function() {
				spyOn(WcHttpRequestService, 'triggerResponseInterceptors');

				expect(WcHttpRequestService.triggerResponseInterceptors).toBeDefined();
			});

			it('should trigger each function in the response interceptor\'s array in index order', function() {
				expect(WcHttpRequestService.responseInterceptors.length).toEqual(0);

				var mockInterceptor1 = function(data) {
					return {data: 'newData'};
				};
				var mockInterceptor2 = function(data) {
					return {data: 'newData2'};
				};

				WcHttpRequestService.addResponseInterceptor(mockInterceptor1);
				WcHttpRequestService.addResponseInterceptor(mockInterceptor2);

				var interceptedParams = WcHttpRequestService.triggerResponseInterceptors('oldData');

				expect(interceptedParams).toEqual({data: 'newData2'});
			});

			it('should not modify the given parameters if none of the response interceptors modify the parameters', function() {
				expect(WcHttpRequestService.responseInterceptors.length).toEqual(0);

				var mockInterceptor1 = function(data) {};

				WcHttpRequestService.addResponseInterceptor(mockInterceptor1);

				var interceptedParams = WcHttpRequestService.triggerResponseInterceptors({data: 'oldData'});

				expect(interceptedParams).toEqual({data: 'oldData'});
			});

			it('should overwrite the data with the returned data from the called interceptor', function() {
				expect(WcHttpRequestService.responseInterceptors.length).toEqual(0);

				var mockInterceptor1 = function(data) {return {thisWillChange: true}};

				WcHttpRequestService.addResponseInterceptor(mockInterceptor1);

				var interceptedParams = WcHttpRequestService.triggerResponseInterceptors({data: 'oldData'});

				expect(interceptedParams).toEqual({thisWillChange: true});
			});

		});

		describe('addErrorInterceptor():', function() {
			it('should define an addErrorInterceptor() function', function() {
				spyOn(WcHttpRequestService, 'addErrorInterceptor');

				expect(WcHttpRequestService.addErrorInterceptor).toBeDefined();
			});

			it('should push a error interceptor to the error interceptor\'s array', function() {
				expect(WcHttpRequestService.errorInterceptors.length).toEqual(0);

				var mockInterceptor = function() {
					return 'I\'m an interceptor!';
				};

				WcHttpRequestService.addErrorInterceptor(mockInterceptor);

				expect(WcHttpRequestService.errorInterceptors.length).toEqual(1);
			});
		});

		describe('triggerErrorInterceptors():', function() {
			it('should define a triggerErrorInterceptors() function', function() {
				spyOn(WcHttpRequestService, 'triggerErrorInterceptors');

				expect(WcHttpRequestService.triggerErrorInterceptors).toBeDefined();
			});
		});

	});

	describe('requestConfigurationMapper():', function() {
		it('should define a requestConfigurationMapper() function', function() {
			spyOn(WcHttpRequestService, 'requestConfigurationMapper');

			expect(WcHttpRequestService.requestConfigurationMapper).toBeDefined();
		});

		it('should return an object containing the base config extended by the passed in config', function() {
			spyOn(WcHttpRequestService, 'requestConfigurationMapper').and.callThrough();

			expect(WcHttpRequestService.requestConfigurationMapper({testParm: 'test'})).toEqual({
				xsrfCookieName: 'WSL-credential',
				withCredentials: true,
				headers: {},
				timeout: 10000,
				testParm: 'test'
			});
		});
	});

	describe('urlBuilder():', function() {
		it('should define a urlBuilder() function', function() {
			spyOn(WcHttpRequestService, 'urlBuilder');

			expect(WcHttpRequestService.urlBuilder).toBeDefined();
		});

		it('should return the concatenation of the baseUrl and the passed in url', function() {
			spyOn(WcHttpRequestService, 'urlBuilder').and.callThrough();

			expect(WcHttpRequestService.urlBuilder('test')).toEqual(WcHttpRequestService.configuration.baseUrl + 'test');
		});

		it('should return the passed in url if it detects a full url (baseUrl is already in the endpoint)', function() {
			spyOn(WcHttpRequestService, 'urlBuilder').and.callThrough();
			var testFullUrl = 'http://www.test.com/test';
			expect(WcHttpRequestService.urlBuilder(testFullUrl)).toEqual(testFullUrl);
			var testFullUrl2 = 'https://www.test.com/test';
			expect(WcHttpRequestService.urlBuilder(testFullUrl2)).toEqual(testFullUrl2);
		});

	});

	describe('get():', function() {
		it('should define a get() function', function() {
			spyOn(WcHttpRequestService, 'get');

			expect(WcHttpRequestService.get).toBeDefined();
		});

		it('should call $http\'s get function with the generated parameters from urlBuilder and requestConfigurationMapper', function() {
			spyOn(WcHttpRequestService, 'get').and.callThrough();
			spyOn(WcHttpRequestService, 'urlBuilder').and.callThrough();
			spyOn(WcHttpRequestService, 'requestConfigurationMapper').and.callThrough();
			//need to return an object here with a then function, even if we aren't using it.
			spyOn($http, 'get').and.returnValue({
				then: function() {
				}
			});

			WcHttpRequestService.get('test', {testParam: 'test'});
			var builtUrl = WcHttpRequestService.urlBuilder('test');
			var mappedConfig = WcHttpRequestService.requestConfigurationMapper({testParam: 'test'});
			expect($http.get).toHaveBeenCalledWith(builtUrl, mappedConfig);
		});

		it('should return data when calling through $http successfully', function() {
			spyOn(WcHttpRequestService, 'get').and.callThrough();
			spyOn($http, 'get').and.callThrough();

			var actualResponse;
			WcHttpRequestService.get('test').then(function(response) {
				actualResponse = response;
			});

			$httpBackend.flush();

			expect(actualResponse).toEqual('success!');
		});

		it('should trigger an error when calling through $http fails', function() {
			spyOn(WcHttpRequestService, 'get').and.callThrough();
			spyOn($http, 'get').and.callThrough();
			//note that the actual value returned here does not matter, just need to fake it out
			spyOn(WcHttpRequestService, 'getNetworkState').and.callFake(function() {
				return $q.when('offline');
			});

			var actualResponse;
			WcHttpRequestService.get('testError').then(null, function(error) {
				actualResponse = error.data;
			});

			$httpBackend.flush();

			expect(actualResponse).toEqual('fail!');
		});

		it('should trigger localStorage caching of the retrieved data if the cache parameter in the config is set to localStorage', function() {
			WcHttpRequestService.get('test', {cache: 'localStorage'});

			$httpBackend.flush();

			expect($window.localStorage.getItem('test')).toEqual('success!');
		});

		it('should retrieve previously cached data as a fallback for always refreshing data in localStorage when the cache parameter is localStorage and the app is offline', function() {
			$window.localStorage.setItem('testError', 'cached data!');
			spyOn(WcHttpRequestService, 'getNetworkState').and.callFake(function() {
				return $q.when('offline');
			});

			var actualResponse;
			WcHttpRequestService.get('testError', {cache: 'localStorage', alwaysRefresh: true}).then(function(response) {
				actualResponse = response;
			});

			$httpBackend.flush();
			expect(actualResponse).toEqual('cached data!');
		});

		it('should reject the promise when cache parameter is localStorage, the app is offline and data is not available in localStorage', function() {
			spyOn(WcHttpRequestService, 'getNetworkState').and.callFake(function() {
				return $q.when('offline');
			});

			var actualResponse;
			WcHttpRequestService.get('testError', {cache: 'localStorage'}).then(null, function(error) {
				actualResponse = error;
			});

			$httpBackend.flush();

			expect(actualResponse).toEqual('Offline: No cached data available!');
		});

		it('should reject the promise when the initial get fails and the ping request succeeds', function() {
			spyOn(WcHttpRequestService, 'getNetworkState').and.callFake(function() {
				return $q.when('unauthenticated');
			});

			var actualResponse;
			WcHttpRequestService.get('testError', {cache: 'localStorage'}).then(null, function(error) {
				actualResponse = error.data;
			});

			$httpBackend.flush();

			expect(actualResponse).toEqual('fail!');
		});

		it('should trigger sessionStorage caching of the retrieved data if the cache parameter in the config is set to sessionStorage', function() {
			WcHttpRequestService.get('test', {cache: 'sessionStorage'});

			$httpBackend.flush();

			expect($window.sessionStorage.getItem('test')).toEqual('success!');
		});

		it('should retrieve previously cached data as a fallback for always refreshings in sessionStorage when the cache parameter is sessionStorage and the app is offline', function() {
			$window.sessionStorage.setItem('testError', 'cached data!');
			spyOn(WcHttpRequestService, 'getNetworkState').and.callFake(function() {
				return $q.when('offline');
			});

			var actualResponse;
			WcHttpRequestService.get('testError', {cache: 'sessionStorage', alwaysRefresh: true}).then(function(response) {
				actualResponse = response;
			});

			$httpBackend.flush();
			expect(actualResponse).toEqual('cached data!');
		});

		it('should use the cache as a default source of requested information when the app is online', function() {
			$window.sessionStorage.setItem('testData', 'cached data!');
			spyOn($http,'get');

			var actualResponse;
			WcHttpRequestService.get('testData', {cache: 'sessionStorage'}).then(function(response) {
				actualResponse = response;
			});

			$rootScope.$apply();

			expect($http.get).not.toHaveBeenCalled();
			expect(actualResponse).toEqual('cached data!');
		});

		it('should re-request data if forceRefresh is set', function() {
			$window.sessionStorage.setItem('testData', 'cached data!');
			spyOn($http,'get').and.callThrough();

			var actualResponse;
			WcHttpRequestService.get('test', {cache: 'sessionStorage', forceRefresh: true}).then(function(response) {
				actualResponse = response;
			});

			$httpBackend.flush();

			expect($http.get).toHaveBeenCalled();
			expect(actualResponse).toEqual('success!');
		});

		it('should request data from the server if not found in cache', function() {
			spyOn($http,'get').and.callThrough();
			expect($window.sessionStorage.getItem('testData')).toBe(null);

			var actualResponse;
			WcHttpRequestService.get('test', {cache: 'sessionStorage'}).then(function(response) {
				actualResponse = response;
			});

			$httpBackend.flush();

			expect($http.get).toHaveBeenCalled();
			expect(actualResponse).toEqual('success!');
		});

		it('should reject the promise when cache parameter is sessionStorage, the app is offline and data is not available in sessionStorage', function() {
			spyOn(WcHttpRequestService, 'getNetworkState').and.callFake(function() {
				return $q.when('offline');
			});

			var actualResponse;
			WcHttpRequestService.get('testError', {cache: 'sessionStorage'}).then(null, function(error) {
				actualResponse = error;
			});

			$httpBackend.flush();

			expect(actualResponse).toEqual('Offline: No cached data available!');
		});

		it('should reject the promise when the initial get fails and the ping request succeeds', function() {
			spyOn(WcHttpRequestService, 'getNetworkState').and.callFake(function() {
				return $q.when('unauthenticated');
			});

			var actualResponse;
			WcHttpRequestService.get('testError', {cache: 'sessionStorage'}).then(null, function(error) {
				actualResponse = error.data;
			});

			$httpBackend.flush();

			expect(actualResponse).toEqual('fail!');
		});

		it('should trigger request interceptors when sending a $http GET', function(){
			spyOn(WcHttpRequestService,'triggerRequestInterceptors').and.callThrough();

			WcHttpRequestService.get('test');

			expect(WcHttpRequestService.triggerRequestInterceptors).toHaveBeenCalled();
		});

		it('should trigger response interceptors when a GET call succeeds', function(){
			spyOn(WcHttpRequestService,'triggerResponseInterceptors').and.callThrough();

			WcHttpRequestService.get('test');

			$httpBackend.flush();

			expect(WcHttpRequestService.triggerResponseInterceptors).toHaveBeenCalled();
		});

		it('should trigger error interceptors when a GET call fails', function(){
			spyOn(WcHttpRequestService,'triggerErrorInterceptors').and.callThrough();

			WcHttpRequestService.get('testError');

			$httpBackend.flush();

			expect(WcHttpRequestService.triggerErrorInterceptors).toHaveBeenCalled();
		});

	});

	describe('post():', function() {
		it('should define a post() function', function() {
			spyOn(WcHttpRequestService, 'post');

			expect(WcHttpRequestService.post).toBeDefined();
		});

		it('should call $http\'s post function with the data and the generated parameters from urlBuilder and requestConfigurationMapper', function() {
			spyOn(WcHttpRequestService, 'post').and.callThrough();
			spyOn(WcHttpRequestService, 'urlBuilder').and.callThrough();
			spyOn(WcHttpRequestService, 'requestConfigurationMapper').and.callThrough();
			//need to return an object here with a then function, even if we aren't using it.
			spyOn($http, 'post').and.returnValue({
				then: function() {
				}
			});

			WcHttpRequestService.post('test', {data: 'testdata'}, {testParam: 'test'});
			var builtUrl = WcHttpRequestService.urlBuilder('test');
			var mappedConfig = WcHttpRequestService.requestConfigurationMapper({testParam: 'test'});
			expect($http.post).toHaveBeenCalledWith(builtUrl, {data: 'testdata'}, mappedConfig);
		});

		it('should return the server\'s success message when calling through $http successfully', function() {
			spyOn(WcHttpRequestService, 'post').and.callThrough();
			spyOn($http, 'post').and.callThrough();

			var actualResponse;
			WcHttpRequestService.post('test', {data: 'testdata'}).then(function(response) {
				actualResponse = response;
			});

			$httpBackend.flush();

			expect(actualResponse).toEqual({data: 'success!', status: 200});
		});

		it('should trigger an error when calling through $http fails', function() {
			spyOn(WcHttpRequestService, 'post').and.callThrough();
			spyOn($http, 'post').and.callThrough();

			var actualResponse;
			WcHttpRequestService.post('testError', {data: 'testdata'}).then(null, function(error) {
				actualResponse = error.data;
			});

			$httpBackend.flush();

			expect(actualResponse).toEqual('fail!');
		});

		it('should trigger request interceptors when sending a $http POST', function(){
			spyOn(WcHttpRequestService,'triggerRequestInterceptors').and.callThrough();

			WcHttpRequestService.post('test',{test: 'testData'});

			expect(WcHttpRequestService.triggerRequestInterceptors).toHaveBeenCalled();
		});

		it('should trigger response interceptors when a POST call succeeds', function(){
			spyOn(WcHttpRequestService,'triggerResponseInterceptors').and.callThrough();

			WcHttpRequestService.post('test',{test: 'testData'});

			$httpBackend.flush();

			expect(WcHttpRequestService.triggerResponseInterceptors).toHaveBeenCalled();
		});

		it('should trigger error interceptors when a POST call fails', function(){
			spyOn(WcHttpRequestService,'triggerErrorInterceptors').and.callThrough();

			WcHttpRequestService.post('testError',{test: 'testData'});

			$httpBackend.flush();

			expect(WcHttpRequestService.triggerErrorInterceptors).toHaveBeenCalled();
		});
	});

	describe('put():', function() {
		it('should define a put() function', function() {
			spyOn(WcHttpRequestService, 'put');

			expect(WcHttpRequestService.put).toBeDefined();
		});

		it('should call $http\'s put function with the data and the generated parameters from urlBuilder and requestConfigurationMapper', function() {
			spyOn(WcHttpRequestService, 'put').and.callThrough();
			spyOn(WcHttpRequestService, 'urlBuilder').and.callThrough();
			spyOn(WcHttpRequestService, 'requestConfigurationMapper').and.callThrough();
			//need to return an object here with a then function, even if we aren't using it.
			spyOn($http, 'put').and.returnValue({
				then: function() {
				}
			});

			WcHttpRequestService.put('test', {data: 'testdata'}, {testParam: 'test'});
			var builtUrl = WcHttpRequestService.urlBuilder('test');
			var mappedConfig = WcHttpRequestService.requestConfigurationMapper({testParam: 'test'});
			expect($http.put).toHaveBeenCalledWith(builtUrl, {data: 'testdata'}, mappedConfig);
		});

		it('should return the server\'s success message when calling through $http successfully', function() {
			spyOn(WcHttpRequestService, 'put').and.callThrough();
			spyOn($http, 'put').and.callThrough();

			var actualResponse;
			WcHttpRequestService.put('test', {data: 'testdata'}).then(function(response) {
				actualResponse = response;
			});

			$httpBackend.flush();

			expect(actualResponse).toEqual({data: 'success!', status: 200});
		});

		it('should trigger an error when calling through $http fails', function() {
			spyOn(WcHttpRequestService, 'put').and.callThrough();
			spyOn($http, 'put').and.callThrough();

			var actualResponse;
			WcHttpRequestService.put('testError', {data: 'testdata'}).then(null, function(error) {
				actualResponse = error.data;
			});

			$httpBackend.flush();

			expect(actualResponse).toEqual('fail!');
		});

		it('should trigger request interceptors when sending a $http PUT', function(){
			spyOn(WcHttpRequestService,'triggerRequestInterceptors').and.callThrough();

			WcHttpRequestService.put('test',{test: 'testData'});

			expect(WcHttpRequestService.triggerRequestInterceptors).toHaveBeenCalled();
		});

		it('should trigger response interceptors when a PUT call succeeds', function(){
			spyOn(WcHttpRequestService,'triggerResponseInterceptors').and.callThrough();

			WcHttpRequestService.put('test',{test: 'testData'});

			$httpBackend.flush();

			expect(WcHttpRequestService.triggerResponseInterceptors).toHaveBeenCalled();
		});

		it('should trigger error interceptors when a PUT call fails', function(){
			spyOn(WcHttpRequestService,'triggerErrorInterceptors').and.callThrough();

			WcHttpRequestService.put('testError',{test: 'testData'});

			$httpBackend.flush();

			expect(WcHttpRequestService.triggerErrorInterceptors).toHaveBeenCalled();
		});

	});

	describe('delete():', function() {
		it('should define a delete() function', function() {
			spyOn(WcHttpRequestService, 'delete');

			expect(WcHttpRequestService.delete).toBeDefined();
		});

		it('should call $http\'s delete function with the generated parameters from urlBuilder and requestConfigurationMapper', function() {
			spyOn(WcHttpRequestService, 'delete').and.callThrough();
			spyOn(WcHttpRequestService, 'urlBuilder').and.callThrough();
			spyOn(WcHttpRequestService, 'requestConfigurationMapper').and.callThrough();
			//need to return an object here with a then function, even if we aren't using it.
			spyOn($http, 'delete').and.returnValue({
				then: function() {
				}
			});

			WcHttpRequestService.delete('test', {testParam: 'test'});
			var builtUrl = WcHttpRequestService.urlBuilder('test');
			var mappedConfig = WcHttpRequestService.requestConfigurationMapper({testParam: 'test'});
			expect($http.delete).toHaveBeenCalledWith(builtUrl, mappedConfig);
		});

		it('should return the server\'s success message when calling through $http successfully', function() {
			spyOn(WcHttpRequestService, 'post').and.callThrough();
			spyOn($http, 'post').and.callThrough();

			var actualResponse;
			WcHttpRequestService.post('test', {data: 'testdata'}).then(function(response) {
				actualResponse = response;
			});

			$httpBackend.flush();

			expect(actualResponse).toEqual({data: 'success!', status: 200});
		});

		it('should trigger an error when calling through $http fails', function() {
			spyOn(WcHttpRequestService, 'delete').and.callThrough();
			spyOn($http, 'delete').and.callThrough();

			var actualResponse;
			WcHttpRequestService.delete('testError').then(null, function(error) {
				actualResponse = error.data;
			});

			$httpBackend.flush();

			expect(actualResponse).toEqual('fail!');
		});

		it('should trigger request interceptors when sending a $http DELETE', function(){
			spyOn(WcHttpRequestService,'triggerRequestInterceptors').and.callThrough();

			WcHttpRequestService.delete('test');

			expect(WcHttpRequestService.triggerRequestInterceptors).toHaveBeenCalled();
		});

		it('should trigger response interceptors when a DELETE call succeeds', function(){
			spyOn(WcHttpRequestService,'triggerResponseInterceptors').and.callThrough();

			WcHttpRequestService.delete('test');

			$httpBackend.flush();

			expect(WcHttpRequestService.triggerResponseInterceptors).toHaveBeenCalled();
		});

		it('should trigger error interceptors when a DELETE call fails', function(){
			spyOn(WcHttpRequestService,'triggerErrorInterceptors').and.callThrough();

			WcHttpRequestService.delete('testError');

			$httpBackend.flush();

			expect(WcHttpRequestService.triggerErrorInterceptors).toHaveBeenCalled();
		});

	});

	describe('Caching:', function() {
		describe('getFromLocalStorage():', function() {
			it('should define a getFromLocalStorage() function', function() {
				spyOn(WcHttpRequestService, 'getFromLocalStorage');

				expect(WcHttpRequestService.getFromLocalStorage).toBeDefined();
			});

			it('should retrieve data from localStorage using the given key', function() {
				var testObj = {testParam: 'test'};
				WcHttpRequestService.storeInLocalStorage('test', testObj);
				expect(WcHttpRequestService.getFromLocalStorage('test')).toEqual(testObj);
			});
		});
		describe('getFromSessionStorage():', function() {
			it('should define a getFromSessionStorage() function', function() {
				spyOn(WcHttpRequestService, 'getFromSessionStorage');

				expect(WcHttpRequestService.getFromSessionStorage).toBeDefined();
			});

			it('should retrieve data from sessionStorage using the given key', function() {
				var testObj = {testParam: 'test'};
				WcHttpRequestService.storeInSessionStorage('test', testObj);
				expect(WcHttpRequestService.getFromSessionStorage('test')).toEqual(testObj);
			});
		});
		describe('storeInLocalStorage():', function() {
			it('should define a storeInLocalStorage() function', function() {
				spyOn(WcHttpRequestService, 'storeInLocalStorage');

				expect(WcHttpRequestService.storeInLocalStorage).toBeDefined();
			});

			it('should store the given data under the given key in localStorage', function() {
				WcHttpRequestService.storeInLocalStorage("test", {testParam: 'test'});
				expect($window.localStorage.getItem('test')).toEqual(angular.toJson({testParam: 'test'}));
			});

			it('should call angular.toJson only when the passed in data is not a string', function() {
				spyOn(angular, 'toJson');

				WcHttpRequestService.storeInLocalStorage('test', 'string');
				expect(angular.toJson).not.toHaveBeenCalled();
				WcHttpRequestService.storeInLocalStorage('test', {testParam: 'test'});
				expect(angular.toJson).toHaveBeenCalled();
				WcHttpRequestService.storeInLocalStorage('test', ['test']);
				expect(angular.toJson).toHaveBeenCalled();
			});
		});
		describe('storeInSessionStorage():', function() {
			it('should define a storeInSessionStorage() function', function() {
				spyOn(WcHttpRequestService, 'getFromSessionStorage');

				expect(WcHttpRequestService.getFromSessionStorage).toBeDefined();
			});

			it('should store the given data under the given key in sessionStorage', function() {
				WcHttpRequestService.storeInSessionStorage("test", {testParam: 'test'});
				expect($window.sessionStorage.getItem('test')).toEqual(angular.toJson({testParam: 'test'}));
			});

			it('should call angular.toJson only when the passed in data is not a string', function() {
				spyOn(angular, 'toJson');

				WcHttpRequestService.storeInSessionStorage('test', 'string');
				expect(angular.toJson).not.toHaveBeenCalled();
				WcHttpRequestService.storeInSessionStorage('test', {testParam: 'test'});
				expect(angular.toJson).toHaveBeenCalled();
				WcHttpRequestService.storeInSessionStorage('test', ['test']);
				expect(angular.toJson).toHaveBeenCalled();
			});
		});
	});
});