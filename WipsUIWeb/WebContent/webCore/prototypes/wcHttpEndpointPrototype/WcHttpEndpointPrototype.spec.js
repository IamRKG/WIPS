'use strict';

describe('WebCoreModule WcHttpEndpointPrototype:', function() {
	var WcHttpEndpointPrototype, WcHttpRequestService;

	beforeEach(function() {
		module('WebCoreModule');
		module('WebCoreModule');


		inject(function($injector) {
			WcHttpEndpointPrototype = $injector.get('WcHttpEndpointPrototype');
			WcHttpRequestService = $injector.get('WcHttpRequestService');
		});
	});

	describe('Constructor:', function() {
		it('should be defined', function() {
			expect(angular.isFunction(WcHttpEndpointPrototype)).toBe(true);
		});

		it('should add the passed in url to the object', function() {
			var testEndpoint = new WcHttpEndpointPrototype('testurl');

			expect(testEndpoint.route).toEqual('testurl');
		});

		it('should include the new baseUrl if one is provided to the constructor', function() {
			var testEndpoint = new WcHttpEndpointPrototype('testurl','http://www.test.com/');
			expect(testEndpoint.route).toEqual('http://www.test.com/testurl');
		});

		it('should not include the new baseUrl if one is not provided to the constructor', function() {
			var testEndpoint = new WcHttpEndpointPrototype('testurl');
			expect(testEndpoint.route).toEqual('testurl');
		});

	});

	describe('Function [subRoute]:', function() {
		it('should be defined', function() {
			expect(angular.isFunction(WcHttpEndpointPrototype.prototype.subRoute)).toBe(true);
		});

		it('should return a new Endpoint with the given sub route appended to the current endpoint', function() {
			var testEndpoint = new WcHttpEndpointPrototype('testurl');
			var newEndpoint = testEndpoint.subRoute('subRoute');
			expect(newEndpoint.route).toEqual('testurl/subRoute');

			var testEndpoint2 = new WcHttpEndpointPrototype('http://www.test.com/testurl');
			var newEndpoint2 = testEndpoint2.subRoute('subRoute');
			expect(newEndpoint2.route).toEqual('http://www.test.com/testurl/subRoute');
		});
	});


	describe('Function [get]:', function() {
		it('should be defined', function() {
			expect(angular.isFunction(WcHttpEndpointPrototype.prototype.get)).toBe(true);
		});

		it('should call WcHttpRequestService with the expected parameters', function() {
			var testEndpoint = new WcHttpEndpointPrototype('testurl');

			spyOn(WcHttpRequestService, 'get');

			testEndpoint.get({cache: 'localStorage'});
			expect(WcHttpRequestService.get).toHaveBeenCalledWith('testurl', {cache: 'localStorage'});
		});
	});

	describe('Function [post]:', function() {
		it('should be defined', function() {
			expect(angular.isFunction(WcHttpEndpointPrototype.prototype.post)).toBe(true);
		});

		it('should call WcHttpRequestService with the expected parameters', function() {
			var testEndpoint = new WcHttpEndpointPrototype('testurl');

			spyOn(WcHttpRequestService, 'post');

			testEndpoint.post({data: 'testdata'}, {headers: {}});
			expect(WcHttpRequestService.post).toHaveBeenCalledWith('testurl', {data: 'testdata'}, {headers: {}});
		});
	});

	describe('Function [put]:', function() {
		it('should be defined', function() {
			expect(angular.isFunction(WcHttpEndpointPrototype.prototype.put)).toBe(true);
		});

		it('should call WcHttpRequestService with the expected parameters', function() {
			var testEndpoint = new WcHttpEndpointPrototype('testurl');

			spyOn(WcHttpRequestService, 'put');

			testEndpoint.put('id', {data: 'testdata'}, {timeout: 1000});
			expect(WcHttpRequestService.put).toHaveBeenCalledWith('testurl/id', {data: 'testdata'}, {timeout: 1000});
		});
	});

	describe('Function [delete]:', function() {
		it('should be defined', function() {
			expect(angular.isFunction(WcHttpEndpointPrototype.prototype.delete)).toBe(true);
		});

		it('should call WcHttpRequestService with the expected parameters', function() {
			var testEndpoint = new WcHttpEndpointPrototype('testurl');

			spyOn(WcHttpRequestService, 'delete');

			testEndpoint.delete('id');
			expect(WcHttpRequestService.delete).toHaveBeenCalledWith('testurl/id', undefined);
		});
	});
});