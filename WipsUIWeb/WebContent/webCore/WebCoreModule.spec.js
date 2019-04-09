'use strict';

describe('WebCoreModule:', function() {
	var WebCoreModule;

	beforeEach(function() {
		WebCoreModule = angular.module('WebCoreModule');
	});

	it('should be registered', function() {
		expect(WebCoreModule).toBeDefined();
	});

	describe('jQuery Helpers:', function() {
		var testHTML = '<div id="testDiv"><p class="testChild">Test Content</p></div>';

		beforeEach(function() {
			//leave for later...
		});

		it('should have an exists function', function() {
			expect(jQuery.fn.exists).toBeDefined();
		});

		it('should have an outerHTML function', function() {
			expect(jQuery.fn.outerHTML).toBeDefined();
		});

		it('should have an exists function that will return false when given bogus HTML', function() {
			var bogusHTML = $(testHTML).find('bogusHTML');
			expect(bogusHTML.exists()).toBeFalsy();
		});

		it('should have an exists function that will return true when given real HTML', function() {
			var bogusHTML = $(testHTML).find('.testChild');
			expect(bogusHTML.exists()).toBeTruthy();
		});

		it('should have an outerHTML function that will have a return value equal to testHTML', function() {
			var HTML = $(testHTML);
			expect(HTML.outerHTML()).toEqual(testHTML);
		});
	});

	describe('Constant:', function() {
		var _;

		beforeEach(function() {
			module('WebCoreModule');

			inject(function(___) {
				_ = ___;
			});
		});

		it('should have a reference to underscore', inject(function(_) {
			expect(_).toBeDefined();
		}));

		it('should have a reference to underscores map function', inject(function(_) {
			expect(_.map).toBeDefined();
		}));
	});
});