'use strict';

var LoginPage= require('./enterLoginInformation.page.e2e.js');

describe('Login page',function(){
	
	var loginPage = new LoginPage();
	
	beforeEach(function() {			
		loginPage.load();
	});
	
	it('should display current page',function(){
		expect(loginPage.isCurrentPage()).toBeTruthy();
		
	});
	
	it('should login and navigate to home page', function() {
		expect(loginPage.navigateToHome());
	});
	
});