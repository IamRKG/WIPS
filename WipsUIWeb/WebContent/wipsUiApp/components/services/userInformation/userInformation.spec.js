'use strict';

describe('WipsUiApp.ComponentsModule User:',function(){
	var User, $window;
	
	var userInformationMock = {
			"userRacfId":"w#e0na",
			"pendingApprovals": [{
					"categoryCode":"25",
					"categoryName": "ATPs",
					"totalCount": "3"
				},
				{
					"categoryCode":"102",
					"categoryName": "Lumpsums",
					"totalCount": "1"
				}],
			"jobDetail":{
				"jobCode":"GN7A",
				"jobName":"ARAVIND"
			}
		
		};
	
	beforeEach(function(){
		// Module & Providers
		module('WipsUiApp.ComponentsModule');
		
		inject(function($injector) {
			User = $injector.get('User');
			$window = $injector.get('$window');
		});
		
		User.setCachedUserInfo(userInformationMock);
		
	});
	
	it('Shoude defines a User', function(){
		expect(User).toBeDefined();
	});
	
	describe('setCachedUserInfo():',function(){
		it('Shoude set user information',function(){
			User.setCachedUserInfo(userInformationMock);
	
			expect(User.userInformation.jobDetail.jobCode).toEqual(userInformationMock.jobDetail.jobCode);
			expect(angular.fromJson($window.sessionStorage.user).jobDetail.jobCode).toEqual(userInformationMock.jobDetail.jobCode);
		});
	});		

	describe('getClearUserInformation():',function(){
		it('Shoude clear user information',function(){
			spyOn($window.sessionStorage, 'clear').and.callThrough();
			User.getClearUserInformation(userInformationMock);
			
			expect($window.sessionStorage.clear).toHaveBeenCalled();
			
			expect(User.userInformation).toBeUndefined();
			
		});
	});		

	
});