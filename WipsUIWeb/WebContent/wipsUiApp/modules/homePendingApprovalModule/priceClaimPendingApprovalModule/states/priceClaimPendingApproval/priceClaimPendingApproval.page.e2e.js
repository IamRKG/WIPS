'use strict';

var PriceClaimPendingApproval = function(){

};

PriceClaimPendingApproval.prototype = Object.create({},{

	isCurrentPage:{
		value: function(){
			return element(by.tagName('h1')).getText().then(function(text){
				return text == 'Price Claims Pending Approval';
			});
		}
	},
	columnHeadersText: {
		value: function() {
			//use map implementation for array of promise handling as talked about here:
			//http://stackoverflow.com/questions/21736191/protractor-how-to-get-the-result-of-an-array-of-promises-into-another-array
			return element.all(by.css('th')).map(function(elm){
				return elm.getText();
			});
		}
	},

	tableRows: {
		get: function() {
			return element.all(by.css('#price-claim-approval-table tbody tr'));
		}
	},

	viewLinks: {
		get: function() {
			return element.all(by.css('#price-claim-approval-table tbody tr td a'));
		}
	},
	navigateToPriceClaimDetailPage: {
		value: function(){
			return this.viewLinks.first().click();
		}
	}
});

module.exports = PriceClaimPendingApproval;

