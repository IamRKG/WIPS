'use strict';

var LoginPage= require('../../../../loginModule/states/login/enterLoginInformation/enterLoginInformation.page.e2e.js');
var HomePendingApprovalPage = require('../../../states/homePendingApproval/HomePendingApproval.page.e2e.js');
var ATPPendingApprovalPage = require('../atpPendingApproval/ATPPendingApproval.page.e2e.js');
var ATPStrategyDetailsPage = require('../atpStrategyDetails/ATPStrategyDetails.page.e2e.js');
var ATPRecapPage = require('./ATPRecap.page.e2e.js');

describe('ATP Recap Page', function() {
	
	var loginPage = new LoginPage();
	var homePendingApprovalPage = new HomePendingApprovalPage();
	var atpPendingApprovalPage = new ATPPendingApprovalPage();
	var atpRecapPage = new ATPRecapPage();
	var atpStrategyDetailsPage = new ATPStrategyDetailsPage();
	
	beforeEach(function() {			
		loginPage.load();
		loginPage.navigateToHome();
		homePendingApprovalPage.navigateToATPListPage();
		atpPendingApprovalPage.navigateToATPRecapPage();
	});
	
	
	it('Should display the current page', function() {
		expect(atpRecapPage.isCurrentPage()).toBeTruthy();
	});
	
	it('Should display the strategy details button in ATP recap page',function(){
		expect(atpRecapPage.isStrategyDetailsButton().isPresent()).toBe(true);
	});
	
	it('Should display the strategy details page',function(){
		atpRecapPage.navigateToStrategyDetailsPage();
		expect(atpStrategyDetailsPage.isCurrentPage()).toBeTruthy();
	});
});