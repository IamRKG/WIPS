/**
 * @ngdoc filter
 * @name WebCoreModule.filter:WcPrice
 * @requires _
 * @description
 * The wcPrice filter assists with view filtering of any price related information.
 */
'use strict';
angular.module('WebCoreModule').
filter('WcPrice', ['_', function(_) {

	// Helper Functions
	function detectFilterType(min, max) {
		/*
			Possible return values:
				range, min, max, invalid
		*/
		var filterType = '';

		if ((min || min === 0) && (max || max === 0)) {
			filterType = 'range';
		} else if (min && (max === null)) {
			filterType = 'min';
		} else if (max && (min === null)) {
			filterType = 'max';
		} else {
			filterType = 'invalid';
		}

		return filterType;
	}

	function filterMax(items, max, propertyName) {
		if (propertyName) {
			return _.filter(items, function(num) {
				if (num[propertyName] <= max) {
					return true;
				}
			});
		} else {
			return _.filter(items, function(num) {
				if (num <= max) {
					return true;
				}
			});
		}
	}

	function filterMin(items, min, propertyName) {
		if (propertyName) {
			return _.filter(items, function(num) {
				if (num[propertyName] >= min) {
					return true;
				}
			});
		} else {
			return _.filter(items, function(num) {
				if (num >= min) {
					return true;
				}
			});
		}
	}

	function filterRange(items, min, max, propertyName) {
		if (propertyName) {
			return _.filter(items, function(num) {
				if (num[propertyName] >= min && num[propertyName] <= max) {
					return true;
				}
			});
		} else {
			return _.filter(items, function(num) {
				if (num >= min && num <= max) {
					return true;
				}
			});
		}
	}


	/*
		item: array of items to filter
		min: null or number
		max: null of number
		propertyName: if specified > assume array of objects : let function know where to find price in object. otherwise assume items are an array of simple values
		at least min or max is required - if both are present, calculate range
		example usage : var filteredArray = $filter('WcPrice')(originalArray, null, 500, 'price');
	*/

	return function(items, min, max, propertyName) {

		var newItems = [];

		// Error Checking
		if (min === undefined || isNaN(min)) {
			min = null;
		}

		if (max === undefined || isNaN(max)) {
			max = null;
		}

		// Detect Filter Type
		var filterType = detectFilterType(min, max);

		switch (filterType) {
			case 'range':
				newItems = filterRange(items, min, max, propertyName);
				break;
			case 'min':
				newItems = filterMin(items, min, propertyName);
				break;
			case 'max':
				newItems = filterMax(items, max, propertyName);
				break;
			case 'invalid':
				newItems = items;
				break;
		}

		return newItems;
	};



}]);