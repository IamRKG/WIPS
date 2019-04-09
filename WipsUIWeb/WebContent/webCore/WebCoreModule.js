/**
 * @ngdoc overview
 * @name WebCoreModule
 * @description
 * WebCore is a collection of internally developed components and extensions and is the majority of the custom code developed for the
 * Ford Responsive Framework (FRF). The WebCoreModule includes all needed vendor scripts as dependencies and also includes the custom
 * developed scripts. Applications can take advantage of all vendor and custom code via inclusion of WebCoreModule.
 */
'use strict';
angular.module('WebCoreModule', [
	'pascalprecht.translate',
	'ui.router',
	'ngTouch',
	'ui.bootstrap',
	'ui.mask',
	'ui.select',
	'angularGrid',
	'ui.grid',
	'ui.grid.selection',
	'ui.grid.pagination',
	'ui.grid.edit',
	'ui.grid.infiniteScroll',
	'smart-table'
]);

/**
 * @ngdoc service
 * @name WebCoreModule.service:_
 * @description
 * WebCore includes the underscore library. Since it's not defined in the Angular context, we define an Angular constant for it here.
 */
angular.module('WebCoreModule').constant('_', window._);
/**
 * @ngdoc service
 * @name WebCoreModule.service:Constants
 * @description
 * WebCore includes a few constant values that are applicable module-wide. They are defined here as a constant service for use
 * throughout the module.
 */
angular.module('WebCoreModule').constant('Constants', {
		'local': 'local',
		'wslBaseUrl': 'https://www.wsl.ford.com/login.cgi?portstripping=no&back=',
		'authorization': {
			'any': 'Any',
			'all': 'All'
		},
		'version': 'Ford Responsive Framework (FRF) 1.0.0'
	});

jQuery.fn.exists = function() {
	return this.length !== 0;
};

jQuery.fn.outerHTML = function() {
	return jQuery('<div>').append(this.eq(0).clone()).html();
};

//credit to stack overflow posters 'Michael Herold and Ryan O'Neill': http://stackoverflow.com/a/15311842
angular.merge = function (dst) {
	angular.forEach(arguments, function(obj) {
		if (obj !== dst) {
			angular.forEach(obj, function(value, key) {
				if (dst[key] && dst[key].constructor && dst[key].constructor === Object) {
					angular.merge(dst[key], value);
				} else {
					dst[key] = value;
				}
			});
		}
	});
	return dst;
};
