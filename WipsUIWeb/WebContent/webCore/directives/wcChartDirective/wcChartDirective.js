/**
 * @ngdoc directive
 * @name WebCoreModule.directive:wcChart
 * @restrict E
 * @scope
 * @param {array} data The array of data to render a chart from
 * @param {string} format The type of chart to render. Can be Line, Bar or Pie
 * @param {object} options An object with configuration options for the chart
 * @param {array=} events Array of objects containing event definitions. Objects should contain two keys, 'event' and 'handler'
 * @param {function=} draw A function to manipulate data as the chart is drawn
 * @param {array=} responsiveOptions Array of arrays with css breakpoint definitions for the chart
 * @description
 * The wcChart directive utilizes the Chartist js library to provide charting capabilities via an Angular directive.
 * The directive provides customization for various chart types via the passed in attributes on the tag.
 */
'use strict';
angular.module('WebCoreModule')
	.directive('wcChart', function() {
		return {
			restrict: 'E',
			scope: {
				data: '&data',
				format: '@format',
				events: '&events',
				draw: '&draw',
				options: '&options',
				responsiveOptions: '&responsiveOptions'
			},
			controller: [ '$scope', '$window', '$timeout',
				function($scope, $window, $timeout) {

					if(!$window.Chartist) {
						throw('Missing required dependency - Chartist.js');
					}

					this.data = $scope.data();
					this.format = $scope.format;
					this.events = $scope.events() || [];
					this.options = $scope.options() || null;
					this.responsiveOptions = $scope.responsiveOptions() || null;

					this.bindEvents = function(chart) {
						this.events.forEach(function(curVal) {
							var eventHandler = function() {
								curVal.handler();
								$timeout(function(){$scope.$apply();});
							};
							chart.bind(curVal.name, eventHandler);
						});
					};

					this.render = function($element) {
						return Chartist[this.format]($element, this.data, this.options, this.responsiveOptions);
					};
				}
			],
			link: function($scope, $element, $attrs, ctrl) {

				// Create Chart
				var element = $element[0];
				var chart = ctrl.render(element);
				ctrl.bindEvents($element);

				// Add Draw Event Separately
				var drawFunc = $scope.draw();
				if (drawFunc) {
					chart.on('draw',drawFunc);
				}

				// Watch for Changes to Data Set
				$scope.$watch(
					function(){
						return $scope.data();
					},
					function(newData, oldData){
						if(newData !== oldData) {
							chart.update();
						}
				}, true);
			}
		};
	});