'use strict';

var LoginPage = function () {
};

LoginPage.prototype = Object.create({}, {
	
	load: {
		value: function() {
			return browser.get('#/login').then(function(){
				return browser.wait(function(){
					return element(by.tagName('button')).isPresent();
				},3000);
			});	
		}
	},
	
	isCurrentPage:{
		value: function(){
			return element(by.tagName('button')).getText().then(function(text){
				return text == 'Login';
			});
		}
	},
	
	login: {
		value: function(){
			element(by.name('loginId')).click();
			element(by.name('loginPassword')).click();
			element(by.name('loginId')).sendKeys('w#e0na');
			element(by.name('loginPassword')).sendKeys('wipsq216');
		}
	},
	navigateToHome: {
		value: function() {
			this.login();
			element(by.id('login')).click();
            browser.waitForAngular();
		}		
	}
	
});

module.exports = LoginPage;