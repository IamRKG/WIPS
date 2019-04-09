'use strict';
describe('WipsUiApp.ATPPendingApproval.ComponentsModule ATPRecapServices:',function(){

	//Dependencies
	var WcHttpEndpointPrototype, WcAlertConsoleService, $q, $state, $rootScope, $translate;
	var ATPRecapServices, WipsHttpStatusCodesServices,WipsHttpStatusCodesConstant,User;

	//Test Data
	var testRecap = {
		"g53xTransactionOutput":{
			"approvers":[
				{"jobCode":"GA7D","approver":"PRIYA","date":"Aug-25-2016","remarks":"Yes"},
				{"jobCode":"GA2A","approver":"PRIYA","date":"Aug-25-2016","remarks":"No"},
				{"jobCode":"GABA","approver":"PRIYA","date":"Aug-25-2016","remarks":"Yes"},
				{"jobCode":"GC0A","approver":"PRIYA","date":"Aug-25-2016","remarks":"No"},
				{"jobCode":"GS0B","approver":"HARI","date":"","remarks":"No"}],
			"suppliers":[
				{"newPrice":"20,442.50668","priceChange":"","annualCost":"165,073,252+","sourcingPercentage":"100%","authority":"Full Authority","webquoteAttachment":"NO","ppc":" 2016-Q3 Partial","usdLiteral":"USD","costSign":"+","recapType":"01","siteCode":"AC7SA","siteName":"AMARA RAJA BATT"}],
			"autoPo":"Yes",
			"capacityShortfallFlag":false,
			"effectiveDate":"Aug-25-2016",
			"part":"QWM02-QA01-AA",
			"reasonCode":"D DESIGN",
			"strategy":"SINGLE SOURCING",
			"totalCost":"165,073,252 USD",
			"atpNumber":"XNLB7A",
			"hasMoreSuppliers":false,
			"hasMoreApprovers":false,
			"rawEffectiveDate":"160825",
			"errorFlag":false
		},
		"status":"OK",
		"errorFlag":false,
		data:{
			"errorMessage":'test',
		}
	}

	beforeEach(function() {
		// Module & Providers
		module('WipsUiApp.ATPPendingApproval.ComponentsModule');

		inject(function($injector) {
			ATPRecapServices = $injector.get('ATPRecapServices');
			WcHttpEndpointPrototype = $injector.get('WcHttpEndpointPrototype');
			WcAlertConsoleService = $injector.get('WcAlertConsoleService');
			User = $injector.get('User');
			WipsHttpStatusCodesServices = $injector.get('WipsHttpStatusCodesServices');
			$q = $injector.get('$q');
			$state = $injector.get('$state');
			$rootScope = $injector.get('$rootScope');
			$translate = $injector.get('$translate');
			WipsHttpStatusCodesServices = $injector.get('WipsHttpStatusCodesServices');
			WipsHttpStatusCodesConstant = $injector.get('WipsHttpStatusCodesConstant');
		});

	});

	it('define a ATPRecapServices', function() {
		expect(ATPRecapServices).toBeDefined();
	});

	it('establishes a endpoint for AtpApproval/Recap', function() {
		expect(ATPRecapServices.atpRecapEndpoint.route).toEqual('AtpApproval/Recap');
	});

	describe('getATPPendingApproval():', function() {

		it('should take given object and create list if the request has been success',function(){

			spyOn(ATPRecapServices.atpRecapEndpoint,'subRoute').and.callFake(function(){
				return {get: function(){return $q.when(testRecap);}};
			});

			var actualResponse={};
			ATPRecapServices.getATPRecap('dzd2T0IxRzdpY3M9', 'XM6BM3').then(function(response) {
				actualResponse = response;
			}, function() {
				console.error('This should not happen');
			});

			testRecap.status = '200';

			$rootScope.$apply();

			expect(ATPRecapServices.atpRecapEndpoint.subRoute).toHaveBeenCalledWith('dzd2T0IxRzdpY3M9/XM6BM3');

		});

		it('should take given object and create a danger alert if the request has not been completed',function(){

			spyOn(ATPRecapServices.atpRecapEndpoint,'subRoute').and.callFake(function(){
				return {get: function(){return $q.when(testRecap);}};
			});

			var actualResponse={};
			ATPRecapServices.getATPRecap('dzd2T0IxRzdpY3M9', 'XM6BM3').then(function(response) {
				actualResponse = response;
			}, function() {
				console.error('This should not happen');
			});


			spyOn($translate, 'instant').and.callFake(function() {
				return 'test';
			});

			testRecap.status = WipsHttpStatusCodesConstant.statusTextAccepted;

			spyOn(WcAlertConsoleService, 'addMessage');

			$rootScope.$apply();
			expect(WipsHttpStatusCodesConstant.statusTextAccepted).toEqual('Accepted');
			expect(ATPRecapServices.atpRecapEndpoint.subRoute).toHaveBeenCalledWith('dzd2T0IxRzdpY3M9/XM6BM3');
			expect(WcAlertConsoleService.addMessage).toHaveBeenCalledWith({message: 'test', type: 'danger', multiple: false});
		});
	})

});
