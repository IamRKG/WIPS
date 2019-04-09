'use strict';

var LoginPage= require('../../../../loginModule/states/login/enterLoginInformation/enterLoginInformation.page.e2e.js');
var HomePendingApprovalPage = require('../../../states/homePendingApproval/HomePendingApproval.page.e2e.js');
var ATPPendingApprovalPage = require('../priceClaimPendingApproval/priceClaimPendingApproval.page.e2e.js');
var ATPRecapPage = require('./priceClaimDetailsApprovals.page.e2e.js');
describe('Price Claim Details Approval Page', function() {
	var loginPage = new LoginPage();
	var homePendingApprovalPage = new HomePendingApprovalPage();
	var PriceClaimPendingApprovalPage = new PriceClaimPendingApprovalPage();
	beforeEach(function() {			
		loginPage.load();
		loginPage.navigateToHome();
		homePendingApprovalPage.navigateToPriceClaimListPage();
		PriceClaimPendingApprovalPage.navigateToPriceClaimDetailPage();
	});
	it('Should display the current page', function() {
		expect(atpRecapPage.isCurrentPage()).toBeTruthy();
	});
});