/**
 * @ngdoc directive
 * @name WebCoreModule.directive:wcClickOnce
 * @restrict A
 * @priority -1
 * @description
 * The wcClickOnce directive is applied as an attribute on any item a user can interact with. It will ensure the user
 * cannot accidentally double click the control. This is handy in preventing double submissions of forms or actions.
 */
'use strict';
angular.module('WebCoreModule')
	.directive('wcClickOnce', ['$timeout', function($timeout) {
		var delay = 500;   // min milliseconds between clicks

		return {
			restrict: 'A',
			priority: -1,   // cause out postLink function to execute before native `ngClick`'s
							// ensuring that we can stop the propagation of the 'click' event
							// before it reaches `ngClick`'s listener
			link: function(scope, elem) {
				var disabled = false;

				var onClick = function(evt) {
					if(disabled) {
						evt.preventDefault();
						evt.stopImmediatePropagation();
					} else {
						disabled = true;
						$timeout(function() {
							disabled = false;
						}, delay, false);
					}
				};


				scope.$on('$destroy', function() {
					elem.off('click', onClick);
				});
				elem.on('click', onClick);
			}
		};

	}]);
