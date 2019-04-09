/**
 * @ngdoc directive
 * @name WebCoreModule.directive:wcLoadingIndicator
 * @restrict A
 * @description
 * The wcLoadingIndicator directive includes the html for a visual loading indicator and a cover to prevent the user from
 * clicking on things as the app loads. The directive adds interceptors to the request/response process, which are used
 * to hide and show the indicator accordingly.
 */
angular.module('WebCoreModule')
	.directive('wcLoadingIndicator', function() {
		return {
			restrict: 'A',
			template: '<div id="loading-cover"></div><div id="loading-indicator"><span>Loading...</span></div>',
			controller: ['$scope', 'WcHttpRequestService', function($scope, WcHttpRequestService) {

				var requestCount = 0;
				var pendingStateChange = false;

				this.showLoadingIndicator = function() {
					$('#loading-cover').show().animate({opacity: 1.0}, 300);
					$('#loading-indicator').show().animate({opacity: 1.0}, 300);
				};

				this.hideLoadingIndicator = function() {
					$('#loading-indicator').hide().animate({opacity:0}, 10);
					$('#loading-cover').hide().animate({opacity:0}, 10);
				};

				this.incrementPendingCounterAndShow = angular.bind(this, function() {
					//increment the counter
					requestCount++;

					//only call to show the indicator when incrementing from 0 to 1.
					//will not need to do so for subsequent calls, as it will already be shown
					if(requestCount === 1) {
						this.showLoadingIndicator();
					}
				});

				this.decrementPendingCounterAndHide = angular.bind(this, function() {
					//decrement the counter
					requestCount--;

					//only call to hide the indicator when decrementing from 1 to 0.
					if(requestCount === 0) {
						this.hideLoadingIndicator();
					}
				});

				WcHttpRequestService.addRequestInterceptor(angular.bind(this, function() {
					var url = arguments[0];
					var config = arguments[2];

					this.incrementPendingCounterAndShow();
				}));

				WcHttpRequestService.addErrorInterceptor(angular.bind(this, function() {
					if(!pendingStateChange) {
						this.decrementPendingCounterAndHide();
					}
				}));

				WcHttpRequestService.addResponseInterceptor(angular.bind(this, function() {
					var data = arguments[0];

					if(!pendingStateChange) {
						this.decrementPendingCounterAndHide();
					}
					return data;
				}));

				$scope.$on('$stateChangeStart', angular.bind(this, function() {
					pendingStateChange = true;
				}));

				this.hideLoadingIndicatorCallback = angular.bind(this, function() {
					requestCount = 0;
					pendingStateChange = false;
					this.hideLoadingIndicator();
				});

				$scope.$on('$stateChangeError', this.hideLoadingIndicatorCallback);
				$scope.$on('$viewContentLoaded', this.hideLoadingIndicatorCallback);
				$scope.$on('Unauthenticated', this.hideLoadingIndicatorCallback);
				$scope.$on('Offline', this.hideLoadingIndicatorCallback);

				return $scope.loadingIndicator = this;
			}]
		};
	});
