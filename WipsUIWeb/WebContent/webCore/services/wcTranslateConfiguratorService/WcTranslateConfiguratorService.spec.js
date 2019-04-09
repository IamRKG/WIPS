'use strict';

describe('WebCoreModule WcTranslateConfiguratorService:', function() {
	describe('WcTranslateConfiguratorServiceProvider: ', function(){
		var wcTranslateConfiguratorServiceProvider, translateProvider;

		beforeEach(function() {
			module('WebCoreModule');
			var testModule = angular.module('testModule', ['WebCoreModule']);
			testModule.config(['WcTranslateConfiguratorServiceProvider', '$translateProvider', function(WcTranslateConfiguratorServiceProvider, $translateProvider){
				wcTranslateConfiguratorServiceProvider = WcTranslateConfiguratorServiceProvider;
				translateProvider = $translateProvider;
			}]);
			module('testModule');

			inject(function($injector) {});
		});

		it('should be available as a provider in a module configuration function', function() {
			expect(wcTranslateConfiguratorServiceProvider).toBeDefined();
		});

		it('should expose the translateProvider', function() {
			expect(wcTranslateConfiguratorServiceProvider.translateProvider).toEqual(translateProvider);
		});

		it('should define a $get array for the configurator service factory', function(){
			expect(typeof wcTranslateConfiguratorServiceProvider.$get).toEqual('object');
			expect(wcTranslateConfiguratorServiceProvider.$get.length).toEqual(3);
			expect(typeof wcTranslateConfiguratorServiceProvider.$get[2]).toEqual('function');
		});

		describe('configureTranslateService(): ', function(){
			it('should call the appropriate translateProvider setup functions when configureTranslateService() is called', function() {
				spyOn(translateProvider, 'useLoader');
				spyOn(translateProvider, 'registerAvailableLanguageKeys');
				spyOn(translateProvider, 'determinePreferredLanguage');
				spyOn(translateProvider, 'fallbackLanguage');
				spyOn(translateProvider, 'use');

				wcTranslateConfiguratorServiceProvider.configureTranslateService();

				expect(translateProvider.useLoader).toHaveBeenCalled();
				expect(translateProvider.registerAvailableLanguageKeys).toHaveBeenCalled();
				expect(translateProvider.determinePreferredLanguage).toHaveBeenCalled();
				expect(translateProvider.fallbackLanguage).toHaveBeenCalled();
				expect(translateProvider.use).toHaveBeenCalled();
			});

			it('if no config object is passed, it should use the default config options when configureTranslateService() is called', function() {
				spyOn(translateProvider, 'useLoader');
				spyOn(translateProvider, 'registerAvailableLanguageKeys');
				spyOn(translateProvider, 'determinePreferredLanguage');
				spyOn(translateProvider, 'fallbackLanguage');
				spyOn(translateProvider, 'use');

				wcTranslateConfiguratorServiceProvider.configureTranslateService();

				expect(translateProvider.useLoader).toHaveBeenCalledWith('$translatePartialLoader', {urlTemplate: 'translations/{lang}/{part}.json'});
				expect(translateProvider.registerAvailableLanguageKeys).toHaveBeenCalledWith(['en', 'zh', 'ru'], {
					'en_*': 'en',
					'en-*': 'en',
					'zh_CN': 'zh',
					'zh_HK': 'zh',
					'ru_RU': 'ru'
				});
				expect(translateProvider.determinePreferredLanguage).toHaveBeenCalledWith();
				expect(translateProvider.fallbackLanguage).toHaveBeenCalledWith('en');
				expect(translateProvider.use).toHaveBeenCalledWith(translateProvider.preferredLanguage());
			});

			it('if a config object is passed, it should use the passed config options when configureTranslateService() is called', function() {
				spyOn(translateProvider, 'useLoader');
				spyOn(translateProvider, 'registerAvailableLanguageKeys');
				spyOn(translateProvider, 'determinePreferredLanguage');
				spyOn(translateProvider, 'fallbackLanguage');
				spyOn(translateProvider, 'use');

				wcTranslateConfiguratorServiceProvider.configureTranslateService({
					loader: 'this',
					loaderObj: 'is',
					langKeyArray: 'all',
					langKeyObj: 'different',
					fallbackLang: 'data'
				});

				expect(translateProvider.useLoader).toHaveBeenCalledWith('this', 'is');
				expect(translateProvider.registerAvailableLanguageKeys).toHaveBeenCalledWith('all', 'different');
				expect(translateProvider.determinePreferredLanguage).toHaveBeenCalledWith();
				expect(translateProvider.fallbackLanguage).toHaveBeenCalledWith('data');
				expect(translateProvider.use).toHaveBeenCalledWith(translateProvider.preferredLanguage());
			});
		});
	});

	describe('WcTranslateConfiguratorService: ', function(){
		var translate, translatePartialLoader, wcTranslateConfiguratorService;

		beforeEach(function() {
			module('WebCoreModule');

			inject(function($injector) {
				translate = $injector.get('$translate');
				translatePartialLoader = $injector.get('$translatePartialLoader');
				wcTranslateConfiguratorService = $injector.get('WcTranslateConfiguratorService');
			});
		});

		it('should be defined', function() {
			expect(wcTranslateConfiguratorService).toBeDefined();
		});

		it('should expose loadPartAndRefresh()', function(){
			expect(typeof wcTranslateConfiguratorService.loadPartAndRefresh).toEqual('function');
		});

		it('should trigger calls to the appropriate translate functions when calling loadPartAndRefresh()', function(){
			spyOn(translatePartialLoader, 'addPart');
			spyOn(translate, 'refresh');

			wcTranslateConfiguratorService.loadPartAndRefresh('testString');

			expect(translatePartialLoader.addPart).toHaveBeenCalledWith('testString');
			expect(translate.refresh).toHaveBeenCalled();
		});
	});
});