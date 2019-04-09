/**
 * @ngdoc directive
 * @name WebCoreModule.directive:wcCheckAllNone
 * @restrict AE
 * @scope
 * @requires datatable
 * @param {string} targetContainerId The html id of the tag this directive should target
 * @description
 * The wcCheckAllNone directive sets up check all/none functionality for an array of checkboxes. It can be used in conjunction
 * with a Datatable. The directive will inject check all/none links and provide the necessary functions to make them function.
 */
'use strict';
angular.module('WebCoreModule')
	.directive('wcCheckAllNone', function() {
		return {
			restrict: 'AE',
			require: '?^datatable',
			scope: {
				targetContainerId: '@targetContainerId'
			},
			template: '<a class="select-all" href="javascript:;" ng-click="checkAll()">{{"actions.selectAll" | translate}}</a> | <a class="select-none" href="javascript:;" ng-click="checkNone()">{{"actions.selectNone"| translate}}</a>',
			controller: ['$scope', function($scope) {
				$scope.toggleCheckAllExists = false;

				$scope.setToggleCheckAllExists = function(exists) {
					$scope.toggleCheckAllExists = exists;
				};

				$scope.getToggleCheckAllExists = function() {
					return $scope.toggleCheckAllExists;
				};

				/* Start Select All Functionality */
				$scope.checkAll = function() {
					if (!$('#' + $scope.targetContainerId + ' input.toggle-check-all').prop('checked') && $scope.getToggleCheckAllExists()) {
						$('#' + $scope.targetContainerId + ' input.toggle-check-all').click();
						console.log('CLICKING TOGGLE CHECK ALL');
					} else {
						$('#' + $scope.targetContainerId).find('input[type="checkbox"]:not(.toggle-check-all)').each(function() {
							var item = arguments[1];
							var $item = $(item);
							if (!$item.prop('checked')) {
								$item.click();
							}
						});
					}
				};
				/* End Select All Functionality */
				/* Start Select None Functionality */
				$scope.checkNone = function() {
					if ($('#' + $scope.targetContainerId + ' input.toggle-check-all').prop('checked') && $scope.getToggleCheckAllExists()) {
						$('#' + $scope.targetContainerId + ' input.toggle-check-all').click();
						console.log('un-CLICKING TOGGLE CHECK ALL');
					} else {
						$('#' + $scope.targetContainerId).find('input[type="checkbox"]:not(.toggle-check-all)').each(function() {
							var item = arguments[1];

							var $item = $(item);
							if ($item.prop('checked')) {
								$item.click();
							}
						});
					}
				};
				/* End Select None Functionality */
			}],
			link: ['$scope', 'iElement', 'iAttrs', 'datatableController', function() {
				var $scope = arguments[0];
				var datatableController = arguments[3];
				if (datatableController) {
					$scope.setToggleCheckAllExists(datatableController.getToggleCheckAllExists());
				}
			}]

		};
	});