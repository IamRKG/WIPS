/**
 * @ngdoc service
 * @name WebCoreModule.service:WcHttpRequestService
 * @requires $rootScope
 * @requires $q
 * @requires $http
 * @requires $window
 * @requires $timeout
 * @description
 * The WcHttpRequestService wraps Angular's $http service. The wrapper provides caching, interceptors and default config params
 * that apply app-wide to all requests.
 */
'use strict';
angular.module('WebCoreModule')
	.service('WcHttpRequestService', ['$rootScope', '$q', '$http', '$window', '$timeout',
		function($rootScope, $q, $http, $window, $timeout) {

			this.configuration = {
				xsrfCookieName: 'WSL-credential',
				withCredentials: true,
				headers: {},
				timeout: 10000,
				baseUrl: '',
				cache: false,
				pingTrustPeriodMS: 1000,
				unAuthenticatedCallback: function() {
				}
			};

			this.configureDefaults = function(config) {
				angular.extend(this.configuration, config);
			};

			this.requestConfigurationMapper = function(config) {
				var httpConfig = angular.copy(this.configuration);
				delete httpConfig.baseUrl;
				delete httpConfig.pingTrustPeriodMS;
				delete httpConfig.unAuthenticatedCallback;

				angular.extend(httpConfig, config);

				delete httpConfig.cache;

				return httpConfig;
			};

			this.urlBuilder = function(url) {
				// if we detect that the url contains a protocol from the endpoint, it's already a full url
				if(url.indexOf('http://') > -1 || url.indexOf('https://') > -1) {
					return url;
				} else {
					return this.configuration.baseUrl + url;
				}
			};

			this.get = function(url, config) {
				var interceptedParams = this.triggerRequestInterceptors(url, null, config);
				url = interceptedParams.url;
				config = interceptedParams.config;

				// if cache is enabled, should we attempt to use cached data or refresh from the server?
				// set alwaysRefresh to never use cache unless offline
				// set forceRefresh to true to override default behavior and request from the server on demand
				if(config && config.cache && !config.alwaysRefresh && !config.forceRefresh) {
					var cachedData;
					if(config.cache == 'localStorage') {
						cachedData = this.getFromLocalStorage(url);
					}
					else if(config.cache == 'sessionStorage') {
						cachedData = this.getFromSessionStorage(url);
					}
					if(cachedData) {
						return $q.when(cachedData);
					}
				}

				return $http.get(this.urlBuilder(url), this.requestConfigurationMapper(config)).then(angular.bind(this, function(response) {

					response = this.triggerResponseInterceptors(response);

					if(config) {
						if(config.cache == 'localStorage') {
							this.storeInLocalStorage(url, response.data);
						}
						else if(config.cache == 'sessionStorage') {
							this.storeInSessionStorage(url, response.data);
						}
					}
					return response.data;
				}), angular.bind(this, function(error) {

					this.triggerErrorInterceptors(error);

					return this.getNetworkState().then(angular.bind(this, function(state) {
						if(state == 'offline' && config) {
							var cachedData;
							if(config.cache == 'localStorage') {
								cachedData = this.getFromLocalStorage(url);
							}
							else if(config.cache == 'sessionStorage') {
								cachedData = this.getFromSessionStorage(url);
							}

							if(!cachedData) {
								return $q.reject('Offline: No cached data available!');
							}
							else {
								return $q.when(cachedData).finally(function() {
									// we are not rejecting the promise but are still operating 'offline'
									$timeout(function() {
										$rootScope.$broadcast('Offline');
									});
								});
							}
						}
						else {
							return $q.reject(error);
						}
					}));
				}));
			};

			this.post = function(url, data, config) {
				var interceptedParams = this.triggerRequestInterceptors(url, data, config);
				url = interceptedParams.url;
				data = interceptedParams.data;
				config = interceptedParams.config;

				return $http.post(this.urlBuilder(url), data, this.requestConfigurationMapper(config)).then(angular.bind(this, function(response) {
					response = this.triggerResponseInterceptors(response);
					return {data: response.data, status: response.status};
				}), angular.bind(this, function(error) {
					this.triggerErrorInterceptors(error);
					return $q.reject(error);
				}));
			};

			this.put = function(url, data, config) {
				var interceptedParams = this.triggerRequestInterceptors(url, data, config);
				url = interceptedParams.url;
				data = interceptedParams.data;
				config = interceptedParams.config;

				return $http.put(this.urlBuilder(url), data, this.requestConfigurationMapper(config)).then(angular.bind(this, function(response) {
					response = this.triggerResponseInterceptors(response);
					return {data: response.data, status: response.status};
				}), angular.bind(this, function(error) {
					this.triggerErrorInterceptors(error);
					return $q.reject(error);
				}));
			};

			this.delete = function(url, config) {
				var interceptedParams = this.triggerRequestInterceptors(url, null, config);
				url = interceptedParams.url;
				config = interceptedParams.config;

				return $http.delete(this.urlBuilder(url), this.requestConfigurationMapper(config)).then(angular.bind(this, function(response) {
					response = this.triggerResponseInterceptors(response);
					return {data: response.data, status: response.status};
				}), angular.bind(this, function(error) {
					this.triggerErrorInterceptors(error);
					return $q.reject(error);
				}));
			};

			this.getFromLocalStorage = function(key) {
				var cachedData = $window.localStorage.getItem(key);

				try {
					cachedData = angular.fromJson(cachedData);
				}
				finally {
					return cachedData;
				}
			};

			this.getFromSessionStorage = function(key) {
				var cachedData = $window.sessionStorage.getItem(key);

				try {
					cachedData = angular.fromJson(cachedData);
				}
				finally {
					return cachedData;
				}
			};

			this.storeInLocalStorage = function(key, value) {
				if(!angular.isString(value)) {
					value = angular.toJson(value);
				}
				$window.localStorage.setItem(key, value);
			};

			this.storeInSessionStorage = function(key, value) {
				if(!angular.isString(value)) {
					value = angular.toJson(value);
				}
				$window.sessionStorage.setItem(key, value);
			};

			this.requestInterceptors = [];
			this.responseInterceptors = [];
			this.errorInterceptors = [];

			this.addRequestInterceptor = function(interceptor) {
				this.requestInterceptors.push(interceptor);
			};

			this.triggerRequestInterceptors = function(url, data, config) {
				var index = 0;
				while(index < this.requestInterceptors.length) {
					var newParms = this.requestInterceptors[index](url, data, config);
					if(newParms) {
						if(newParms.url) url = newParms.url;
						if(newParms.data) data = newParms.data;
						if(newParms.config) config = newParms.config;
					}
					index++;
				}
				return {url: url, data: data, config: config};
			};

			this.addResponseInterceptor = function(interceptor) {
				this.responseInterceptors.push(interceptor);
			};

			this.triggerResponseInterceptors = function(response) {
				var index = 0;
				while(index < this.responseInterceptors.length) {
					var newResponse = this.responseInterceptors[index](response);
					if(newResponse) {
						response = newResponse;
					}
					index++;
				}
				return response;
			};

			this.addErrorInterceptor = function(interceptor) {
				this.errorInterceptors.push(interceptor);
			};

			/**
			 These 'Interceptors' currently function more as a callback/event handler
			 Keeping name as interceptor to align with Restangular and other function names
			 There is potential for refactoring to support graceful error recovery
			 **/
			this.triggerErrorInterceptors = function(error) {
				var index = 0;
				while(index < this.errorInterceptors.length) {
					this.errorInterceptors[index](error);
					index++;
				}
			};

			/**
			 *
			 *    Network Detection
			 *
			 * **/
			this._lastPingRequestTimestamp = 0;
			this._trustedResponse = '';
			this._pendingRequests = 0;
			this._currentPingRequestPromise = {};
			this._networkStatusResponses = ['online', 'unauthenticated', 'offline'];

			/* can return statuses defined in _networkStatusResponses
			 *
			 * will always resolve promise, never reject - error case is a valid response
			 * */
			this.getNetworkState = function() {

				if(this._pendingRequests > 0) {
					// already pinging server, do not re-request and return saved promise
					return this._currentPingRequestPromise;
				} else {
					var currentTime = new Date();
					if(currentTime - this._lastPingRequestTimestamp > this.configuration.pingTrustPeriodMS) {
						this._pendingRequests++;
						this._lastPingRequestTimestamp = currentTime;
						this._trustedResponse = '';
					} else {
						// we are trusting that our last ping request was close enough
						return $q.when(this._trustedResponse);
					}
				}

				this._currentPingRequestPromise = $http.get(this.urlBuilder('unprotected/ping')).then(angular.bind(this, function() {
					// we only query the network when there is an error in retrieving data from the server
					// if we can access the unprotected ping endpoint, we know the issue is most likely that the user is not logged in
					this._pendingRequests--;
					this._currentPingRequestPromise = {};
					this._trustedResponse = this._networkStatusResponses[1]; //unauthenticated
					// perform callback
					this.configuration.unAuthenticatedCallback();
					$rootScope.$broadcast('Unauthenticated');
					return $q.when(this._networkStatusResponses[1]);
				}), angular.bind(this, function() {
					// if we cannot access the unprotected ping endpoint, we must be offline
					this._pendingRequests--;
					this._currentPingRequestPromise = {};
					this._trustedResponse = this._networkStatusResponses[2]; // offline
					$rootScope.$broadcast('Offline');
					return $q.when(this._networkStatusResponses[2]);
				}));

				return this._currentPingRequestPromise;

			};


		}]);
