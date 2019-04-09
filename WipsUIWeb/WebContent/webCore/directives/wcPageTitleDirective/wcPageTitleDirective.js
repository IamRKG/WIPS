/**
 * @ngdoc directive
 * @name WebCoreModule.directive:wcPageTitle
 * @restrict A
 * @description
 * The wcPageTitle directive will update the page's title by modifying the text of the title tag to include the name of the
 * application (the app-wide module name) and the current state of the application.
 */
'use strict';
angular.module('WebCoreModule')
	.directive('wcPageTitle', function() {
		return {
			restrict: 'A',
			controller: ['$scope', '$state', '$compile', function($scope, $state, $compile) {

				var firstRun = true;

				$scope.pageTitle = '';

				this.cleanUpName = function(name) {

					var cleanName = name;

					cleanName = cleanName.replace(/-/g, ' ');

					cleanName = cleanName.replace(/\b./g, function(m) {
						return m.toUpperCase();
					});

					return cleanName;
				};

				this.updatePageTitle = function() {

					// grab app name
					var appName = $('#ng-app').attr('ng-app').replace('Module','');

					// grab state name
					var stateName = this.cleanUpName($state.$current.name);

					// grab parent state's name (if applicable - e.g. workflows)
					var parentStateName;
					if($state.$current.parent) {
						parentStateName = this.cleanUpName($state.get($state.$current.parent).name);
					}

					var tempPageTitle;

					if(parentStateName && (parentStateName.replace(/ /g,'') != appName)) {
						tempPageTitle = parentStateName + ': ' + stateName + ' | ' + appName;
					} else {
						tempPageTitle = stateName + ' | ' + appName;
					}

					$scope.pageTitle = tempPageTitle;

				};

				$scope.$on('$viewContentLoaded', angular.bind(this, function() {
					if(firstRun) {
						var titleTag = $('title').attr({'wc-page-title':'','ng-bind':'pageTitle'});
						titleTag.replaceWith($compile(titleTag)($scope));
						firstRun = false;
					}
					this.updatePageTitle();
				}));

			}]

		};
	});