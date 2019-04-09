'use strict';

var LoginPage= require('../../../loginModule/states/login/enterLoginInformation/enterLoginInformation.page.e2e.js');
var LumpSumPendingApproval = require('../../lumpSumPendingApprovalModule/states/lumpSumPendingApproval/lumpSumPendingApproval.page.e2e.js');
var ATPPendingApprovalPage = require('../../atpPendingApprovalModule/states/atpPendingApproval/ATPPendingApproval.page.e2e.js');
var LumpSumPendingApproval = require('../../lumpSumPendingApprovalModule/states/priceClaimPendingApproval/priceClaimPendingApproval.page.e2e.js');
var HomePendingApprovalPage = require('./HomePendingApproval.page.e2e.js');

describe('Home Pending Approval Page', function() {
	
	var loginPage = new LoginPage();
	var lumpSumPendingApproval = new LumpSumPendingApproval();
	var atpPendingApprovalPage = new ATPPendingApprovalPage();
	var priceClaimPendingApproval = new PriceClaimPendingApproval();
	var homePendingApprovalPage = new HomePendingApprovalPage();
	beforeEach(function(){			
		loginPage.load();
		loginPage.navigateToHome();
	});
	
	/*TODO: Switch button and job details map*/
	
	it('should display the current page', function() {
		expect(homePendingApprovalPage.isCurrentPage()).toBeTruthy();
	});
	it('should display correct columns', function() {
		var customerColumnHeaders = ['Action', 'Entity', 'Total Waiting'];
		//use map implementation for array of promise handling as talked about here:
		//http://stackoverflow.com/questions/21736191/protractor-how-to-get-the-result-of-an-array-of-promises-into-another-array
		homePendingApprovalPage.columnHeadersText().then(function(result){
			for(var i=0; i<result.length; i++){
				expect(result[i]).toEqual(customerColumnHeaders[i]);
			}
		});

	});
	it('should have a view link for every row in the table', function() {
		var numberOfLinks = homePendingApprovalPage.viewLinks.count();
		var numberOfRows = homePendingApprovalPage.tableRows.count();

		expect(numberOfLinks).toEqual(numberOfRows);
	});
	
	it('should navigate to ATP List Page', function() {
		homePendingApprovalPage.navigateToATPListPage();
		expect(atpPendingApprovalPage.isCurrentPage()).toBeTruthy();
	});
	/*it('should navigate to lump sum Page', function() {
		homePendingApprovalPage.navigateToLumpSumListPage();
		expect(lumpSumPendingApproval.isCurrentPage()).toBeTruthy();
	});*/
	it('should navigate to lump sum Page', function() {
		homePendingApprovalPage.navigateToLumpSumListPage();
		expect(lumpSumPendingApproval.isCurrentPage()).toBeTruthy();
	});
	it('should navigate to price claim Page', function() {
		homePendingApprovalPage.navigateToPriceClaimListPage();
		expect(priceClaimPendingApproval.isCurrentPage()).toBeTruthy();
	});
	it('should display button "Switch" and select switch job code', function(){
		expect(homePendingApprovalPage.isSwitchButtonPresent().isPresent()).toBe(true);
		expect(homePendingApprovalPage.selectSwitchJobCode()).toEqual('GN7A');
	});

});

