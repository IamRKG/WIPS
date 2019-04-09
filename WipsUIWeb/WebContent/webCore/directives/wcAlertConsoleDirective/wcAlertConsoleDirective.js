/**
 * @ngdoc directive
 * @name WebCoreModule.directive:wcAlertConsole
 * @restrict A
 * @description
 * The wcAlertConsole directive adds an alert console div to the page. The directive also watches the array of visible
 * messages, and updates the view to display all messages that should currently be visible. This directive works with
 * WebCore's WcAlertConsoleService, which adds the messages into the arrays.
 */
'use strict';
angular.module('WebCoreModule')
	.directive('wcAlertConsole', function() {
		return {
			restrict: 'A',
			templateUrl: 'webCore/directives/wcAlertConsoleDirective/wcAlertConsoleDirectiveTemplate.html',
			replace: false,
			controller: ['$scope', 'WcAlertConsoleService', '$timeout', function($scope, WcAlertConsoleService, $timeout) {

				$scope.visibleMessages = [];

				$scope.closeAlert = function(uniqueishID) {
					// manually trigger fade out
					WcAlertConsoleService.removeMessage(uniqueishID, true);
				};

				if(WcAlertConsoleService.getSettings().removeErrorOnStateChange) {
					$scope.$on('$stateChangeStart', function() {
						WcAlertConsoleService.removeErrorMessages();
					});
				}

				// watch on alert messages
				$scope.$watch(function() {
					var messages = WcAlertConsoleService.getMessages(true);
					return messages;
				}, function(newMsgs, oldMsgs) {
					$scope.visibleMessages = newMsgs;

					// move user to focus on alert if new message is added
					$timeout(function() {
						if(newMsgs.length > oldMsgs.length) {
							var userYpos = $(document).scrollTop();
							var msgYpos = $('div.alert').last().offset();
							msgYpos = msgYpos.top - 15;

							if(msgYpos < userYpos) {
								$('html, body').animate({ scrollTop: msgYpos},100);
							}
						}
					}, 0);

				}, true);


			}]
		};
	});