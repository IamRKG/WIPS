'use strict';

describe('WipsUiApp.ComponentsModule:', function() {
	var WipsUiAppComponentsModule;

	beforeEach(function() {
		WipsUiAppComponentsModule = angular.module('WipsUiApp.ComponentsModule');
	});



	it('should be registered', function() {
		expect(WipsUiAppComponentsModule).toBeDefined();
	});

});