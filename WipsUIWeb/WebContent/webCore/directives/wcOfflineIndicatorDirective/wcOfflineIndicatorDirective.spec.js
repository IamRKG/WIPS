'use strict';

describe('WebCoreModule wcOfflineIndicatorDirective:', function() {

	//Dependencies
	var $compile, $rootScope, WcHttpRequestService, $httpBackend, $window, $filter, $q, WcTranslateConfiguratorService;

	//variable to hold the compiled element
	var compiledElem;

	//variable to hold the compileDirective function
	var compileDirective;

	beforeEach(function() {
		//the loadingIndicatorDirective lives in the components module, so include it first.
		module('WebCoreModule');

		inject(function($injector) {
			$compile = $injector.get('$compile');
			$rootScope = $injector.get('$rootScope');
			WcHttpRequestService = $injector.get('WcHttpRequestService');
			$httpBackend = $injector.get('$httpBackend');
			$window = $injector.get('$window');
			$filter = $injector.get('$filter');
			$q = $injector.get('$q');
			WcTranslateConfiguratorService = $injector.get('WcTranslateConfiguratorService');
		});

		//we need a function to compile a sample of our directive, to test its behaviors
		//for the passed in sample code, this function will compile the chunk against the rootScope
		compileDirective = function(sampleTag) {
			compiledElem = $compile(sampleTag)($rootScope);
			//digest after compiling to ensure changes make their way back to angular
			$rootScope.$digest();
		};
	});

	it('should compile the given template and produce a valid element',function(){
		spyOn(WcTranslateConfiguratorService, 'loadPartAndRefresh');

		compileDirective('<div wc-offline-indicator></div>');
		var offlineIndicator = angular.element(compiledElem).find('#offline-indicator');

		expect(compiledElem).toBeDefined();
		expect(offlineIndicator).toBeDefined();
	});

	it('should show the offline indicator when the "Offline" event is fired',function(){
		spyOn(WcTranslateConfiguratorService, 'loadPartAndRefresh');

		compileDirective('<div wc-offline-indicator></div>');
		spyOn($rootScope.offlineIndicator,'showOfflineIndicator');

		$rootScope.$broadcast('Offline');

		expect($rootScope.offlineIndicator.showOfflineIndicator).toHaveBeenCalled();
	});

	it('should hide the offline indicator when a success ReST call occurs',function(){
		spyOn(WcTranslateConfiguratorService, 'loadPartAndRefresh');

		compileDirective('<div wc-offline-indicator></div>');
		spyOn($rootScope.offlineIndicator,'hideOfflineIndicator');

		$httpBackend.when('GET','test').respond(200,{});

		WcHttpRequestService.get('test');

		$httpBackend.flush();

		expect($rootScope.offlineIndicator.hideOfflineIndicator).toHaveBeenCalled();
	});

	it('should check the network state on demand',function(){
		spyOn(WcTranslateConfiguratorService, 'loadPartAndRefresh');

		compileDirective('<div wc-offline-indicator></div>');

		spyOn(WcHttpRequestService,'getNetworkState').and.callFake(function(){
			return $q.when('offline');
		});

		$rootScope.offlineIndicator.checkNetworkState();

		expect($rootScope.checking).toBe(true);

		$rootScope.$apply();

		expect($rootScope.checking).toBe(false);

		expect(WcHttpRequestService.getNetworkState).toHaveBeenCalled();
	});

	it('should hide the indicator by default',function(){
		spyOn(WcTranslateConfiguratorService, 'loadPartAndRefresh');

		spyOn($.fn,'hide');

		compileDirective('<div wc-offline-indicator></div>');

		expect($.fn.hide).toHaveBeenCalled();
	});

	it('should hide the indicator on demand',function(){
		spyOn(WcTranslateConfiguratorService, 'loadPartAndRefresh');

		compileDirective('<div wc-offline-indicator></div>');
		spyOn($.fn,'hide');

		$rootScope.offlineIndicator.hideOfflineIndicator();

		expect($.fn.hide).toHaveBeenCalled();
	});

	it('should show the indicator on demand',function(){
		spyOn(WcTranslateConfiguratorService, 'loadPartAndRefresh');

		compileDirective('<div wc-offline-indicator></div>');
		spyOn($.fn,'show');

		$rootScope.offlineIndicator.showOfflineIndicator();

		expect($.fn.show).toHaveBeenCalled();
	});

});