/**
 * @ngdoc service
 * @name WebCoreModule.service:WcAuthorizationService
 * @requires WebCoreModule.service:WcHttpEndpointPrototype
 * @requires WebCoreModule.service:WcAuthorizationCacheHelper
 * @requires _
 * @requires $q
 * @requires Constants
 * @requires $state
 * @description
 * The WcAuthorizationService communicates with a specified restful endpoint to determine whether a user is authorized for a
 * resource or not. The service exposes functions to assist in decisions for whole states or even specific resources.
 */
'use strict';
angular.module('WebCoreModule')
	.service('WcAuthorizationService', ['WcHttpEndpointPrototype', 'WcAuthorizationCacheHelper', '_', '$q', 'Constants', '$state', function(WcHttpEndpointPrototype, WcAuthorizationCacheHelper, _, $q, Constants, $state) {
		this.authorizationEndpoint = new WcHttpEndpointPrototype('authorization');

		this.requestAuthorization = function(name) {
			WcAuthorizationCacheHelper.addPolicy(name, this.authorizationEndpoint.get({params: {
				'authorizationString': name
			}}).then(function(response) {
				return response[0];
			}));
		};

		this.isAuthorizedFromCache = function(policy) {
			return WcAuthorizationCacheHelper.retrievePolicyAuthorization(policy);
		};

		/**
		 * @ngdoc method
		 * @name isStateAuthorized
		 * @methodOf WebCoreModule.service:WcAuthorizationService
		 * @param {String} stateName blah
		 * @returns {Promise} asdfasdf
		 */
		this.isStateAuthorized = function(stateName) {

			// Grab applicable policies from state's definition
			var state = $state.get(stateName);
			var policies = null,
				criteria = null;

			if (state.data) {
				policies = state.data.policies ? state.data.policies : null;
				criteria = state.data.criteria ? state.data.criteria : null;
			}

			// If state does not require any policy permissions, return true.
			if (!policies) {
				return $q.when(true);
			}

			// Set criteria to 'All' by default if not specified
			if (policies && !criteria) {
				criteria = Constants.authorization.all;
			}

			return this.isAuthorized(policies, criteria);
		};

		this.isAuthorized = function(policySet, criteria) {

			if (!criteria) {
				criteria = Constants.authorization.all;
			}

			if (!angular.isArray(policySet)) {
				policySet = [policySet];
			}

			var authorizations = [];

			_.each(policySet, angular.bind(this, function(policy) {
				var isCached = this.isAuthorizedFromCache(policy);
				if (isCached === null) {
					this.requestAuthorization(policy);
					isCached = this.isAuthorizedFromCache(policy);
				}
				authorizations.push(isCached);
			}));

			return $q.all(authorizations).then(function(arrayOfAuths) {
				switch (criteria) {
					case 'Any':
						return _.some(arrayOfAuths);
					case 'All':
						return _.every(arrayOfAuths);
					default:
						return false;
				}
			});
		};
	}]);