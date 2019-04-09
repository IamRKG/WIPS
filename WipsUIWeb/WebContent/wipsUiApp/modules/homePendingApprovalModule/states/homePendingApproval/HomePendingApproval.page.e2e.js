'use strict';

var HomePendingApprovalPage = function() {
	angular.module('HomePendingApprovalHTTPMock', ['ngMockE2E', 'WipsUiAppModule'])
	.run(function($httpBackend) {
		$httpBackend.whenPOST('http://localhost:13000/WipsWeb/WIPS/REST/WipsLogin/UserAuth').respond(function(method, url, data) {
		    return [200, '', {}];
		  });
		
		$httpBackend.whenPOST(/.*/).passThrough();
	});

};

var HomePendingApprovalPage = function(){
	
};

HomePendingApprovalPage.prototype = Object.create({},{

	isCurrentPage:{
		value: function(){
			return element(by.tagName('h1')).getText().then(function(text){
				return text == 'Pending Approval';
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
			return element.all(by.css('#home-pending-approval-table tbody tr'));
		}
	},
	
	viewLinks: {
		get: function() {
			return element.all(by.css('#home-pending-approval-table tbody tr td a'));
		}
	},
	
	navigateToATPListPage: {
		value: function(){
			return this.viewLinks.first().click();
		}
	},
	/*TODO need to create three list for home page*/ 
	
	navigateToLumpSumListPage: {
		value: function(){
			return this.viewLinks.get(1).click();
		}
	},
	
	navigateToPriceClaimListPage: {
	value: function(){
		return this.viewLinks.last().click();
	}
	},
	
	isSwitchButtonPresent:{
		value: function(){
			return element(by.name('Switch'));
		}

	},
	selectSwitchJobCode: {
		value: function(){
			return this.isSwitchButtonPresent().click().then(function(){
				return browser.wait(function(){
					return element(by.css('.modal')).isPresent();
				},5000, 'Did not find modal before timeout').then(function(){
					return browser.executeScript('window.scrollTo(0,document.body.scrollHeight)').then(function() {
						return element(by.name('delegatedJobs')).click().then(function(){
							return element(by.id('modal-submit')).click().then(function(){
								return element.all(by.css('.row dl dd')).first().getText();
							});
						});
					});
				});
				
			});
		}
	},
});

module.exports = HomePendingApprovalPage;