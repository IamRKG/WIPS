/**
 * @ngdoc directive
 * @name WebCoreModule.directive:wcPagination
 * @restrict A
 * @priority 1
 * @scope
 * @param {array} wcPagination The array of data to filter through
 * @param {string} location Where pagination controls should be added. Can be top, bottom or both
 * @param {string=} resultsPerPage array literal defining the options that should show in the results per page dropdown
 * @param {string=} resultsPerPageText string defining the text that should display as a label for the results per page dropdown
 * @param {string=} showingResultsText string defining the text that should display in the 'showing results x-y of z' text
 * @param {number=} resultsItemCount number defining the default number of results to show per page
 * @description
 * The wcPagination directive adds pagination controls to a list of data in a view.
 */
'use strict';
angular.module('WebCoreModule')
	.directive('wcPagination', ['$templateCache', '$compile', function($templateCache, $compile) {
		return {
			restrict: 'A',
			priority: 1,
			scope: {
				wcPagination: '=',
				location: '@',
				resultsPerPage: '&',
				resultsPerPageText: '@',
				showingResultsText: '@',
				resultsItemCount: '&',
				responsiveBreakpoints: '&'
			},
			controller: ['$scope', '$window', '_', function($scope, $window, _) {
				$scope.searchResults = $scope.wcPagination;

				if(angular.isArray($scope.resultsPerPage())){
					$scope.resultsPerPage = $scope.resultsPerPage();
				} else {
					//set defaults if a value isn't given
					$scope.resultsPerPage = [10, 25, 50, 100];
				}

				if(angular.isArray($scope.responsiveBreakpoints())){
					$scope.responsiveBreakpoints = $scope.responsiveBreakpoints();
				} else {
					//set defaults if a value isn't given
					$scope.responsiveBreakpoints = [430, 500, 600, 900];
				}

				if(!$scope.resultsPerPageText){
					$scope.resultsPerPageText = 'Results per page: '
				}

				if(!$scope.showingResultsText){
					$scope.showingResultsText = 'Showing results '
				}

				if(angular.isNumber($scope.resultsItemCount())){
					$scope.resultsItemCount = $scope.resultsItemCount();
				} else {
					$scope.resultsItemCount = $scope.resultsPerPage[0];
				}

				$scope.totalItems = $scope.searchResults.length;
				$scope.itemsPerPage = $scope.resultsItemCount;
				$scope.currentPage = 1;
				$scope.boundaryLinks = true;
				$scope.maxSize = 5;
				$scope.filteredSearchResults = [];
				$scope.updatePageResultsCount = function() {
					$scope.itemsPerPage = $scope.resultsItemCount;
				};
				$scope.begin = 1;
				$scope.end = $scope.begin + $scope.itemsPerPage;

				$scope.$watchCollection(function() {
						return $scope.currentPage + $scope.itemsPerPage;
					}, function() {
						$scope.begin = (($scope.currentPage - 1) * $scope.itemsPerPage);
						$scope.end = $scope.begin + $scope.itemsPerPage;
						if($scope.end > $scope.totalItems) {
							$scope.end = $scope.totalItems;
						}
						$scope.wcPagination = $scope.searchResults.slice($scope.begin, $scope.end);
						$window.scrollTo(0, 1);
					}
				);

				$scope.currentBreakpoint = '';
				//responsive stuff
				$scope.$watch(function(){
					return $scope.currentBreakpoint;
				}, function(newValue, oldValue) {
					if(newValue != oldValue) {
						if(newValue == 'xs') {
							$(".pagination-row").addClass("hidden");
							$scope.itemsPerPage = $scope.searchResults.length;
							$scope.maxSize = 0;
							$scope.boundaryLinks = false;
						}
						else if(newValue == 'sm') {
							$(".pagination-row").removeClass("hidden");
							$scope.itemsPerPage = $scope.resultsItemCount;
							$scope.maxSize = 0;
							$scope.boundaryLinks = false;
						}
						else if(newValue == 'md') {
							$(".pagination-row").removeClass("hidden");
							$scope.itemsPerPage = $scope.resultsItemCount;
							$scope.maxSize = 0;
							$scope.boundaryLinks = false;
						}
						else if(newValue == 'lg') {
							$(".pagination-row").removeClass("hidden");
							$scope.itemsPerPage = $scope.resultsItemCount;
							$scope.maxSize = 1;
							$scope.boundaryLinks = true;
						}
						else {
							$scope.maxSize = 5;
							$scope.boundaryLinks = true;
						}
					}
				});

				var resizeHandler = function(){
					//<=430 is default
					if(window.innerWidth <= $scope.responsiveBreakpoints[0] && $scope.currentBreakpoint != 'xs') {
						$scope.currentBreakpoint = 'xs';
						$scope.$apply();
					}
					//>430 and <=500 is default
					else if((window.innerWidth > $scope.responsiveBreakpoints[0] && window.innerWidth <= $scope.responsiveBreakpoints[1]) && $scope.currentBreakpoint != 'sm') {
						$scope.currentBreakpoint = 'sm';
						$scope.$apply();
					}
					//>500 and <=600 is default
					else if((window.innerWidth > $scope.responsiveBreakpoints[1] && window.innerWidth <= $scope.responsiveBreakpoints[2]) && $scope.currentBreakpoint != 'md') {
						$scope.currentBreakpoint = 'md';
						$scope.$apply();
					}
					//>600 and <=900 is default
					else if((window.innerWidth > $scope.responsiveBreakpoints[2] && window.innerWidth <= $scope.responsiveBreakpoints[3]) && $scope.currentBreakpoint != 'lg') {
						$scope.currentBreakpoint = 'lg';
						$scope.$apply();
					}
					//>900 is default
					else if(window.innerWidth > $scope.responsiveBreakpoints[3] && $scope.currentBreakpoint != 'xl') {
						$scope.currentBreakpoint = 'xl';
						$scope.$apply();
					}
					else {
						//can only get here if we haven't changed breakpoints. do nothing.
					}
				};
				//debounce this handler to avoid rapid-fire resize events
				angular.element($window).bind('resize', _.debounce(resizeHandler, 300));

				//finally, call the resizeHandler once to initialize our breakpoint var
				setTimeout(resizeHandler, 0);
			}],
			link: function(scope, elem, attr, ctrl) {
				//leave template here instead of in it's own file, as it's difficult to load the template properly from a file for this type of inclusion.
				var template = '<div class="row pagination-row"><div class="col-sm-4 col-xs-6"><label for="results-per-page" class="control-label">{{resultsPerPageText}}</label><select ng-model="resultsItemCount" ng-options="result for result in resultsPerPage" id="results-per-page" name="resultsPerPage" ng-change="updatePageResultsCount();"></select></div><div class="col-sm-4 hidden-xs text-center results-data">{{showingResultsText}} {{begin+1}}-{{end}} of {{totalItems}}</div><div class="col-sm-4 col-xs-6 text-right"><pagination total-items="totalItems" items-per-page="resultsItemCount" ng-model="currentPage" max-size="maxSize" boundary-links="boundaryLinks" previous-text="&#xe071" next-text="&#xe075" first-text="&#xe069" last-text="&#xe077"></pagination></div></div>';

				if(scope.location == 'top' || scope.location == 'both') {
					elem.before($compile(template)(scope));
				}
				if(scope.location == 'bottom' || scope.location == 'both') {
					elem.after($compile(template)(scope));
				}
			}
		};
	}]);