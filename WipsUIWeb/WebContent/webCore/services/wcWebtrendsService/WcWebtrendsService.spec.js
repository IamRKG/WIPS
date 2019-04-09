'use strict';

xdescribe('WebCoreModule WcWebtrendsService:', function() {

	var WcWebtrendsService;

	// Dependencies
	var $rootScope, $window, $state, $timeout;

	beforeEach(function() {
		module('WebCoreModule', function() {
		});

		inject(function($injector) {
			WcWebtrendsService = $injector.get('WcWebtrendsService');
			$rootScope = $injector.get('$rootScope');
			$window = $injector.get('$window');
			$state = $injector.get('$state');
			$timeout = $injector.get('$timeout');
		});
	});

	afterEach(function() {
		$window.location.hash = '';
		$state.$current = {};
	});

	it('should be defined', function() {
		expect(WcWebtrendsService).toBeDefined();
	});

	it('should call the trackData function on the $viewContentLoaded event if enabled', function() {
		spyOn(WcWebtrendsService, 'trackData');
		WcWebtrendsService.enable();

		$rootScope.$broadcast('$stateChangeSuccess');

		$timeout.flush();

		expect(WcWebtrendsService.trackData).toHaveBeenCalled();
	});

	it('should not call the trackData function on the $viewContentLoaded event if not enabled', function() {
		spyOn(WcWebtrendsService, 'trackData');

		$rootScope.$broadcast('$stateChangeSuccess');

		$timeout.flush();

		expect(WcWebtrendsService.trackData).not.toHaveBeenCalled();
	});

	describe('enable(): ', function() {
		it('should enable Webtrends data tracking', function() {
			expect(WcWebtrendsService._enabled).toEqual(false);

			WcWebtrendsService.enable();

			expect(WcWebtrendsService._enabled).toEqual(true);
		});
	});

	describe('trackData(): ', function() {
		it('should pass page url hash and state name data to Webrends', function() {
			$window.location.hash = '/test';
			$state.$current.name = 'testState';

			var data = WcWebtrendsService.trackData();

			expect(data).toEqual('#/test:testState');
		});

		it('should use the state name when no hash is found', function() {
			$state.$current.url = '/test';
			$state.$current.name = 'testState';

			var data = WcWebtrendsService.trackData();

			expect(data).toEqual(':testState');
		});
	});


});