'use strict';

angular.module('WipsUiApp.ComponentsModule').
service('UserService', ['WcHttpEndpointPrototype','WcHttpRequestService', function(WcHttpEndpointPrototype, WcHttpRequestService) {

	// WcHttpRequestService.configuration.headers.Authorization = "Login"; 
	this.userInformationEndpoint = new WcHttpEndpointPrototype('WipsLogin/UserAuth');
	
}]);