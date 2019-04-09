/**
 * @ngdoc directive
 * @name WebCoreModule.directive:wcProtectedResource
 * @restrict A
 * @param {string=} wcProtectedResource string or array literal defining the resource:action pairs that define authorization for the tag
 * @description
 * The wcProtectedResource directive removes a tag from or adds a tag back to the DOM based on authorization rules. The directive
 * leverages the wcAuthorizatioService to retrieve the authorization status for the current user from a restful endpoint.
 */
'use strict';
angular.module('WebCoreModule')
	.directive('wcProtectedResource', function() {
		return {
			restrict: 'A',
			require: ['wcProtectedResource', '?^wcDataTable'],
			controller: ['WcAuthorizationService', '$element', '$attrs',
				function(WcAuthorizationService, $element, $attrs) {
					this.checkAuthorization = function() {
						var criteria = null;
						var policies = $attrs.wcProtectedResource;

						if (policies) {

							// check to see if we have an authorization criteria in-line
							if (policies.indexOf('|') > -1) {
								// extract "any" or "all"
								criteria = policies.slice(policies.indexOf('|') + 1, policies.length).toString().trim();
							}

							// if we have a criteria, clean up string so we can easily extract policies
							if (criteria) {
								policies = policies.slice(0, policies.indexOf('|'));
							}

							// support multiple policies
							policies = policies.split(',');

							// remove whitespace
							for (var i = 0; i < policies.length; i++) {
								policies[i] = policies[i].trim();
							}

						} else {
							policies = [];
						}

						var isAuth = WcAuthorizationService.isAuthorized(policies, criteria);

						isAuth.then(function(authorized) {
							if (authorized === false) {
								$element.remove();
							}
						});
					};
				}
			],
			compile: function() {
				return {
					post: function() {
						var controllers = arguments[3];
						var protectedResourceCtrl = controllers[0];
						var datatableCtrl = controllers[1];
						if (datatableCtrl) {
							datatableCtrl.checkAuthorization = protectedResourceCtrl.checkAuthorization();
						}
						protectedResourceCtrl.checkAuthorization();
					}

				};
			}
		};
	});