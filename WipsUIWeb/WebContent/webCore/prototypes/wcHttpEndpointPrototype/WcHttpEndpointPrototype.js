/**
 * @ngdoc service
 * @name WebCoreModule.service:WcHttpEndpointPrototype
 * @requires WebCoreModule.service:WcHttpRequestService
 * @description
 * The WcHttpEndpointPrototype wraps the WcHttpRequestService to make calls against a restful endpoint easy. Creating a new
 * endpoint prototype exposes all http verbs for that endpoint, making calls to the restful service easy to manage.
 */
'use strict';
angular.module('WebCoreModule')
	.factory('WcHttpEndpointPrototype', ['WcHttpRequestService', function(WcHttpRequestService) {

		function Endpoint(route, baseURL) {
			this.baseUrl = baseURL;
			this.route = route;
			if(baseURL) {
				this.route = this.baseUrl+this.route;
			}
		}

		Endpoint.prototype = {
			subRoute: function(id) {
				return new Endpoint(this.route + '/' + id);
			},
			get: function(options) {
				return WcHttpRequestService.get(this.route, options);
			},
			post: function(data, options) {
				return WcHttpRequestService.post(this.route, data, options);
			},
			put: function(id, data, options) {
				return WcHttpRequestService.put(this.route+'/'+id, data, options);
			},
			delete: function(id, options){
				return WcHttpRequestService.delete(this.route+'/'+id, options);
			}
		};

		return Endpoint;
	}]);