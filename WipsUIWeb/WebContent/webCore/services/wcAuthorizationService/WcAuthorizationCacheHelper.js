/**
 * @ngdoc service
 * @name WebCoreModule.service:WcAuthorizationCacheHelper
 * @requires _
 * @requires $q
 * @requires $window
 * @description
 * The WcAuthorizationCacheHelper is utilized by the WcAuthorizationService to cache authorization results for the application.
 * Results from authorization checks are stored in the browser's session storage, limiting the number of network requests that are required.
 */
'use strict';
angular.module('WebCoreModule')
	.service('WcAuthorizationCacheHelper', ['_', '$q', '$window', function(_, $q, $window) {

		/**
			Policy Cache Format
			[
				{
					"policy":"DeleteBooking:execute",
					"authorized":false
				}
			]

			requestedPolicies Format
			[
				'resource:action',
				'resource:action',
				'resource:action'
			]
		**/

		this.setSessionCache = function() {
			$window.sessionStorage.policyCache = angular.toJson(this.cache);
		};

		this.getSessionCache = function() {
			return angular.fromJson($window.sessionStorage.policyCache);
		};

		this.cache = this.getSessionCache();

		if (!this.cache) {
			this.cache = [];
		}

		this.resetCache = function() {
			this.cache = [];
		};

		this.retrievePolicyAuthorization = function(formattedPolicy) {
			var foundPolicy = _.findWhere(this.cache, {
				'policy': formattedPolicy
			});
			if (foundPolicy !== undefined) {
				if (foundPolicy.promise && foundPolicy.promise.then) {
					return foundPolicy.promise.then(function(resolved) {
						return resolved.authorized;
					});
				} else {
					return $q.when(foundPolicy.authorized);
				}
			} else {
				return null;
			}
		};

		this.formatPolicy = function(name, promiseOrData) {
			// detect if policy is a promise
			if (promiseOrData.then) {
				promiseOrData.then(angular.bind(this, function(resolvedPolicy) {
					var policyLocation;
					for (policyLocation = 0; policyLocation < this.cache.length; policyLocation++) {
						if (this.cache[policyLocation].policy == name) {
							break;
						}
					}
					this.cache[policyLocation] = this.formatPolicy(name, resolvedPolicy);
					this.setSessionCache();

				}));
				var cachePolicy = {};
				cachePolicy.policy = name;
				cachePolicy.promise = promiseOrData;
				return cachePolicy;
			} else {
				var localPolicy = angular.copy(promiseOrData);
				localPolicy.policy = name;
				delete localPolicy.resource;
				delete localPolicy.action;
				return localPolicy;
			}
		};

		this.addPolicy = function(policyName, policyPromise) {
			this.cache.push(this.formatPolicy(policyName, policyPromise));
			this.setSessionCache();
		};

	}]);