/**
 * @ngdoc service
 * @name WebCoreModule.service:WcWebtrendsService
 * @requires $rootScope
 * @requires $window
 * @requires $state
 * @requires $timeout
 * @description
 * The WcWebtrendsService wraps configuration needed by the Ford webtrends implementation for web analytics.
 */
'use strict';
angular.module('WebCoreModule')
	.service('WcWebtrendsService', ['$rootScope', '$window', '$state', '$timeout', function($rootScope, $window, $state, $timeout) {

		this._enabled = false;

		this.enable = function() {
			this._enabled = true;
		};

		this.trackData = function() {
			var uri, urlPart;

			if ($window.location.hash !== '') {
				urlPart = $window.location.hash + $window.location.search;
			}

			if (urlPart && (urlPart.replace('#/','') !== $state.$current.name)) {
				uri = urlPart + ':' + $state.$current.name;
			} else {
				uri = urlPart;
			}

			if (!uri) {
				uri = ':' + $state.$current.name;
			}

			$window.dcsMultiTrack('DCS.dcsuri', uri);
			return uri;
		};

		$rootScope.$on('$stateChangeSuccess', angular.bind(this, function() {
			if(this._enabled && $window.dcsMultiTrack) {
				// $timeout required to allow Angular to update browser URL
				$timeout(angular.bind(this, function(){
					$rootScope.$apply();
					this.trackData();
				}));
			}
		}));

	}]);