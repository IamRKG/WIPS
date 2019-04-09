/**
 * @ngdoc directive
 * @name WebCoreModule.directive:wcOfflineIndicator
 * @restrict A
 * @description
 * The wcOfflineIndicator directive includes a response interceptor to watch for when requests over the network fail. When things
 * fail, this directive will add an AlertConsole like message to notify the user of the situation.
 */
'use strict';
angular.module('WebCoreModule')
	.directive('wcOfflineIndicator', function() {
		return {
			restrict: 'A',

			template: '<div id="offline-indicator" class="container-fluid" ng-switch on="checking">' +
			'<alert type="warning"><span class="glyphicon glyphicon-warning-sign" ng-switch-when="false"></span><span class="glyphicon glyphicon-refresh glyphicon-refresh-animate" ng-switch-when="true"></span>' +
			'<span>{{\'offlineIndicator.offlineMessage\'|translate}} ' +
			'<a href ng-click="checkNetworkState()">{{\'offlineIndicator.retryConnection\'|translate}}</a></span></alert></div>',
			replace: true,
			controller: ['WcHttpRequestService', '$scope', '$window', '$timeout', 'WcTranslateConfiguratorService',
				function(WcHttpRequestService, $scope, $window, $timeout, WcTranslateConfiguratorService) {

				WcTranslateConfiguratorService.loadPartAndRefresh('WC-offlineIndicator');

				// Ensure directive is hidden initially
				$('#offline-indicator').hide();

				$scope.checking = false;

				this.showOfflineIndicator = function() {
					$('#offline-indicator').show();
					var alertBox = $('#offline-indicator .alert');
					alertBox.addClass('pulse');
					$timeout(function(){
						alertBox.removeClass('pulse');
					},2000);
					$window.scrollTo(0,1);
				};

				this.hideOfflineIndicator = function() {
					$('#offline-indicator').hide();
				};

				this.checkNetworkState = function() {
					$scope.checking = true;
					WcHttpRequestService.getNetworkState().then(function(){
						$scope.checking = false;
					});
				};

				WcHttpRequestService.addResponseInterceptor(angular.bind(this, function(){
					// hide offline mode if we have a successful ReST response
					this.hideOfflineIndicator();
				}));

				$scope.$on('Offline', angular.bind(this, function() {
					this.showOfflineIndicator();
				}));

				return $scope.offlineIndicator = this;
			}],
			link: function(scope, elem, attr, ctrl) {
				scope.checkNetworkState = ctrl.checkNetworkState;
			}
		};
	});
