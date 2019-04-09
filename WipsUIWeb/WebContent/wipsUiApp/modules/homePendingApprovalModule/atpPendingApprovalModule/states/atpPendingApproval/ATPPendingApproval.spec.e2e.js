'use strict';

var LoginPage= require('../../../../loginModule/states/login/enterLoginInformation/enterLoginInformation.page.e2e.js');
var HomePendingApprovalPage = require('../../../states/homePendingApproval/HomePendingApproval.page.e2e.js');
var ATPRecapPage = require('../atpRecap/ATPRecap.page.e2e.js');
var ATPPendingApproval = require('./ATPPendingApproval.page.e2e.js');

describe('ATP Pending Approval Page', function() {
	
	var loginPage = new LoginPage();
	var homePendingApprovalPage = new HomePendingApprovalPage();
	var atpRecapPage = new ATPRecapPage();
	var atpPendingApproval = new ATPPendingApproval();
	
	beforeEach(function() {			
		loginPage.load();
		loginPage.navigateToHome();
		homePendingApprovalPage.navigateToATPListPage();
	});
	
	
	it('should display the current page', function() {
		expect(atpPendingApproval.isCurrentPage()).toBeTruthy();
	});
	
	it('should display the current page instructions', function() {
		expect(atpPendingApproval.isCurrentPageInstruction()).toBeTruthy();
	});
	
	it('should display correct columns', function() {
		var customerColumnHeaders = ['ATP No.', 'Part No.', 'Buyer'];
		//use map implementation for array of promise handling as talked about here:
		//http://stackoverflow.com/questions/21736191/protractor-how-to-get-the-result-of-an-array-of-promises-into-another-array
		atpPendingApproval.columnHeadersText().then(function(result){
			for(var i=0; i<result.length; i++){
				expect(result[i]).toEqual(customerColumnHeaders[i]);
			}
		});

	});
	
	it('should have a view link for every row in the table', function() {
		var numberOfLinks = atpPendingApproval.viewLinks.count();
		var numberOfRows = atpPendingApproval.tableRows.count();

		expect(numberOfLinks).toEqual(numberOfRows);
	}),
	
	it('should navigate to ATP Recap Page', function() {
		atpPendingApproval.navigateToATPRecapPage();
		expect(atpRecapPage.isCurrentPage()).toBeTruthy();
	});
	
});