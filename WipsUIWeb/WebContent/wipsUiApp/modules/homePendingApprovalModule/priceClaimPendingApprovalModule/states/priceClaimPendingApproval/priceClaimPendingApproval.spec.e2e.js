'use strict';

var LoginPage= require('../../../../loginModule/states/login/enterLoginInformation/enterLoginInformation.page.e2e.js');
var HomePendingApprovalPage = require('../../../states/homePendingApproval/HomePendingApproval.page.e2e.js');
var PriceClaimPendingApproval = require('./priceClaimPendingApproval.page.e2e.js');

describe('Price Claim Pending Approval Page', function() {
	var loginPage = new LoginPage();
	var homePendingApprovalPage = new HomePendingApprovalPage();
	var priceClaimPendingApproval = new PriceClaimPendingApproval();

	beforeEach(function() {
		loginPage.load();
		loginPage.navigateToHome();
		homePendingApprovalPage.navigateToATPListPage();
	});

	it('should display the current page',function(){
		expect(priceClaimPendingApproval.isCurrentPage()).toBeTruthy();


	});

	it('should display correct columns', function() {
		var customerColumnHeaders = ['Price Claim No.', 'Supplier', 'Effective Date','Claim Title'];
		//use map implementation for array of promise handling as talked about here:
		//http://stackoverflow.com/questions/21736191/protractor-how-to-get-the-result-of-an-array-of-promises-into-another-array
		atpPendingApproval.columnHeadersText().then(function(result){
			for(var i=0; i<result.length; i++){
				expect(result[i]).toEqual(customerColumnHeaders[i]);
			}
		});

	});

	it('should have a view link for every row in the table', function() {
		var numberOfLinks = priceClaimPendingApproval.viewLinks.count();
		var numberOfRows = priceClaimPendingApproval.tableRows.count();

		expect(numberOfLinks).toEqual(numberOfRows);
	}),

		it('should navigate to Price Claim Detail Page', function() {
			atpPendingApproval.navigateToATPRecapPage();
			expect(atpRecapPage.isCurrentPage()).toBeTruthy();
		});

});
