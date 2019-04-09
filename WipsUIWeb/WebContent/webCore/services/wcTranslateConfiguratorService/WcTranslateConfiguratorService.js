/**
 * @ngdoc service
 * @name WebCoreModule.service:WcTranslateConfiguratorService
 * @requires $translateProvider
 * @description
 * The WcTranslateConfiguratorService allows WebCore to wrap default translation configuration with a construct that is still
 * accessible in a module's .config() function. This provider also exposes a service, allowing for runtime configuration adjustment.
 */
'use strict';
angular.module('WebCoreModule').provider('WcTranslateConfiguratorService', ['$translateProvider', function($translateProvider) {

	//expose the translate provider in case users need to call through to it
	this.translateProvider = $translateProvider;

	this.configuration = {
		loader: '$translatePartialLoader',
		loaderObj: {urlTemplate: 'translations/{lang}/{part}.json'},
		langKeyArray: ['en', 'zh', 'ru'],
		langKeyObj: {
			'en_*': 'en',
			'en-*': 'en',
			'zh_CN': 'zh',
			'zh_HK': 'zh',
			'ru_RU': 'ru'
		},
		fallbackLang: 'en'
	};

	this.configureTranslateService = function(config) {
		//default config is set - if different config is passed in, use it instead
		if(config) {
			angular.extend(this.configuration, config);
		}

		$translateProvider.useLoader(this.configuration.loader, this.configuration.loaderObj);
		$translateProvider.registerAvailableLanguageKeys(this.configuration.langKeyArray, this.configuration.langKeyObj);
		$translateProvider.determinePreferredLanguage();
		$translateProvider.fallbackLanguage(this.configuration.fallbackLang);
		$translateProvider.use($translateProvider.preferredLanguage());
	};

	//this is essentially a factory function
	this.$get = ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader){
		this.loadPartAndRefresh = function(partString) {
			$translatePartialLoader.addPart(partString);
			$translate.refresh();
		};

		//return our object when all is said and done
		return this;
	}];
}]);