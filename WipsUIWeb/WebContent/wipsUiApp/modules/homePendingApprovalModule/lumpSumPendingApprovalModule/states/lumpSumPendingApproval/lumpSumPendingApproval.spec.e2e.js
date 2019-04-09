'use strict';

var LoginPage= require('../../../../loginModule/states/login/enterLoginInformation/enterLoginInformation.page.e2e.js');
var LumpSumPendingApproval = require('./lumpSumPendingApproval.page.e2e.js');

describe('Lump Sum Pending Approval Page', function() {
	
	var loginPage = new LoginPage();
	var lumpSumPendingApproval = new LumpSumPendingApproval();
	
	beforeEach(function() {			
		loginPage.load();
	});
	
	
	it('should display the current page', function() {
		expect(lumpSumPendingApproval.isCurrentPage()).toBeTruthy();
	});
	
	it('should display the current page instructions', function() {
		expect(lumpSumPendingApproval.isCurrentPageInstruction()).toBeTruthy();
	});
	
	it('should display correct columns', function() {
		var customerColumnHeaders = ['Lump Sum No.', 'Buyer', 'Cause','Supplier'];
		//use map implementation for array of promise handling as talked about here:
		//http://stackoverflow.com/questions/21736191/protractor-how-to-get-the-result-of-an-array-of-promises-into-another-array
		lumpSumPendingApproval.columnHeadersText().then(function(result){
			for(var i=0; i<result.length; i++){
				expect(result[i]).toEqual(customerColumnHeaders[i]);
			}
		});

	});
	
	it('should have a view link for every row in the table', function() {
		var numberOfLinks = lumpSumPendingApproval.viewLinks.count();
		var numberOfRows = lumpSumPendingApproval.tableRows.count();

		expect(numberOfLinks).toEqual(numberOfRows);
	});
	
});