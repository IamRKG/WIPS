/**
 * @ngdoc directive
 * @name WebCoreModule.directive:wcNavMenu
 * @restrict A
 * @scope
 * @param {object} menuData The property to assign the menu data into so the view can access it
 * @param {array} menuDataUrl Array of objects defining the menu's structure and text
 * @description
 * The wcNavMenu directive generates a navigation menu from the given menu data url. The url should expose an array of objects
 * defining the menu's structure and text.
 */
angular.module('WebCoreModule')
	.directive('wcNavMenu', ['$compile', function($compile) {
		return {
			restrict: 'A',
			scope: {
				menuData: '=menuData',
				menuDataUrl: '@menuDataUrl'
			},
			transclude: true,
			templateUrl: 'webCore/directives/wcNavMenuDirective/wcNavMenuDirectiveTemplate.html',
			controller: ['$scope', '$element', '$attrs', '$http',
				function($scope, $element, $attrs, $http) {

					$http.get($scope.menuDataUrl).success(function(response) {
						$scope.menuData = response;
					});

					$scope.$on('$stateChangeStart', function() {
						$('nav .in, nav .open').removeClass('in open');
					});

					$scope.$on('collapseNavBar', function() {
						$scope.collapseNavbar();
					});

					$scope.navbarCollapsed = true;

					$scope.toggleNavbarCollpase = function() {
						$scope.navbarCollapsed = !$scope.navbarCollapsed;
						if(!$scope.navbarCollapsed) {
							$("button.navbar-toggle").attr("aria-expanded", "true");
						}
						else {
							$("button.navbar-toggle").attr("aria-expanded", "false");
						}
					};

					$scope.collapseNavbar = function() {
						if(!$scope.navbarCollapsed) {
							$scope.toggleNavbarCollpase();
						}
					};
				}]


		};
	}])
	.directive('subNavigationTree', ['$compile', function($compile) {
		return {
			restrict: 'E',
			scope: true,
			link: function(scope, element, attrs) {
				scope.tree = scope.node;

				if(scope.tree.children && scope.tree.children.length) {
					var template = angular.element('<ul class="dropdown-menu "><li wc-protected-resource="{{node.protected}}" ng-repeat="node in tree.children"><a ui-sref="{{node.state | translate}}" ui-sref-opts="{reload: true, notify: true}">{{node.text | translate}}</a><sub-navigation-tree tree="node"></sub-navigation-tree></li></ul>');

					var linkFunction = $compile(template);
					linkFunction(scope);
					element.replaceWith(template);
				}
				else {
					element.remove();
				}
			}
		};
	}]);
