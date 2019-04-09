'use strict';

describe('WebCoreModule - Price Filter:', function() {

	var WcPrice;

	// Test Data
	var simplePriceArray = [-100, 0, 1, 1.5, '10', 27, 89, 9000];
	var objectPriceArray = [{
		name: 'first',
		price: -100
	}, {
		otherProp: 'last',
		price: '0'
	}, {
		test: 0,
		price: 1
	}, {
		test: 'string',
		price: 1.5
	}, {
		test: 'string',
		price: 100
	}];

	beforeEach(function() {
		module('WebCoreModule');

		inject(function($injector) {
			WcPrice = $injector.get('WcPriceFilter');
		});

	});

	it('should have access to the filter', function() {
		expect(WcPrice).toBeDefined();
	});

	it('should return a subset of array values less than or equal to specified max price', function() {
		var filteredPriceArray = WcPrice(simplePriceArray, null, 10);

		expect(filteredPriceArray).toEqual([-100, 0, 1, 1.5, '10']);
	});

	it('should return a subset of objects array with a property less than or equal to specified max price', function() {
		var filteredPriceArray = WcPrice(objectPriceArray, null, 10, 'price');

		expect(filteredPriceArray).toEqual([{
			name: 'first',
			price: -100
		}, {
			otherProp: 'last',
			price: '0'
		}, {
			test: 0,
			price: 1
		}, {
			test: 'string',
			price: 1.5
		}]);
	});

	it('should return a subset of array values greater than or equal to specified min price', function() {
		var filteredPriceArray = WcPrice(simplePriceArray, 5);

		expect(filteredPriceArray).toEqual(['10', 27, 89, 9000]);
	});

	it('should return a subset of objects array with a property greater than or equal to specified min price', function() {
		var filteredPriceArray = WcPrice(objectPriceArray, 5, undefined, 'price');

		expect(filteredPriceArray).toEqual([{
			test: 'string',
			price: 100
		}]);
	});

	it('should return a subset of array values with respect to a given price range', function() {
		var filteredPriceArray = WcPrice(simplePriceArray, -5, 89);

		expect(filteredPriceArray).toEqual([0, 1, 1.5, '10', 27, 89]);
	});

	it('should return a subset of objects in an array with respect to a given price range', function() {
		var filteredPriceArray = WcPrice(objectPriceArray, 0, 89, 'price');

		expect(filteredPriceArray).toEqual([{
			otherProp: 'last',
			price: '0'
		}, {
			test: 0,
			price: 1
		}, {
			test: 'string',
			price: 1.5
		}]);
	});

	it('should return given array if filter is invalid, i.e. contains a string', function() {
		var filteredPriceArray = WcPrice(simplePriceArray, 0, 'Any');

		expect(filteredPriceArray).toEqual(simplePriceArray);
	});


});