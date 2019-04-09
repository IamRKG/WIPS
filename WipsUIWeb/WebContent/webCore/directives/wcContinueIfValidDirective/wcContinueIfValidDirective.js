/**
 * @ngdoc directive
 * @name WebCoreModule.directive:wcContinueIfValid
 * @restrict A
 * @requires ngController
 * @description
 * The wcContinueIfValid directive encorporates form validation into the submit button action. By applying this directive,
 * a click on the submit button will first check form validation. If the form is valid, the app will continue to the next
 * state defined in the state's 'workflow' data property. If the form is invalid, the app will not navigate and the user
 * will be allowed to make adjustments to their entries before trying navigation again.
 */
'use strict';
angular.module('WebCoreModule')
	.directive('wcContinueIfValid', function() {
		return {
			restrict: 'A',
			required: '^ngController',
			controller: ['$state', '$timeout', function($state, $timeout) {
				this.$state = $state;
				this.$timeout = $timeout;
			}],
			link: function(scope, elem, attr, ctrl) {

				elem.on('click', function() {

					var elemCtrl = elem.controller();
					var formName = elem[0].form.name;
					var formCtrl = scope.$parent[formName];

					// enable display of validation errors
					elemCtrl.isFormSubmitted = true;

					// if form is valid, navigate to requested state and call the given function
					if(formCtrl.$valid) {
						var workflowData = ctrl.$state.$current.data.workflow;

						if(workflowData.controllerContinueCallbackFunctionName) {
							elemCtrl[workflowData.controllerContinueCallbackFunctionName]();
						}

						ctrl.$state.go(workflowData.nextState);
					}

					ctrl.$timeout(function() {
						scope.$apply();
					});

				});

			}
		};
	});
