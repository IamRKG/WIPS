'use strict';

describe('WipsUiApp.ComponentsModule ApplicationPolicyService:',function(){
	var ApplicationPolicyService, UserService, WcAlertConsoleService, $q, $rootScope, $httpBackend;

	var userInformationMock = {
			"userRacfId":"w#e0na",
			"racfPassword":"Ford@123",
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
	
	beforeEach(function() {
		// Module & Providers
		module('WipsUiApp.ComponentsModule');
		module('WipsUiApp.LoginModule');
		module('WipsUiApp.HomePendingApprovalModule');
		
		inject(function($injector) {
			ApplicationPolicyService = $injector.get('ApplicationPolicyService');
			UserService = $injector.get('UserService');
			WcAlertConsoleService = $injector.get('WcAlertConsoleService');
			$q = $injector.get('$q');
			$rootScope = $injector.get('$rootScope');
			$httpBackend = $injector.get('$httpBackend');
		});
		
		$httpBackend.whenPOST('WipsLogin/UserAuth').respond(200,userInformationMock);
	});
	
	it('should define a ApplicationPolicyService', function() {
		expect(ApplicationPolicyService).toBeDefined();
	});
	
	describe('getUserInformation():', function() {

		it('should leverage the UserService to populate the object and go to home page',function(){
			
			var param = {
					racfId: userInformationMock.userRacfId,
					racfPassword: userInformationMock.racfPassword
			};
			
			var userInformationResponse;
			
			spyOn(UserService.userInformationEndpoint, 'post').and.callFake(function() {
				return $q.when({data: userInformationMock});
			});
			
			ApplicationPolicyService.getUserInformation(param).then(function(response) {
				userInformationResponse =  response;
			},function(){
				console.error('This should not happen');
			});
			

			ApplicationPolicyService.getUserInformation(userInformationMock);

			$rootScope.$apply();
			
			expect(UserService.userInformationEndpoint.post).toHaveBeenCalled();
			expect(userInformationResponse).toEqual(userInformationMock);

		});

		it('should leverage the UserService to populate the failure object and add error message in to app message services  ',function(){
			
			var userInfo = {racfId: 'w#e0na', racfPassword: 'Ford@123'};
			
			spyOn(UserService.userInformationEndpoint, 'post').and.callFake(function() {
				return $q.reject({data: {errorMessage:'errorTest'}});
			});
			
			spyOn(WcAlertConsoleService, 'addMessage');
			
		
			ApplicationPolicyService.getUserInformation(userInfo);

			$rootScope.$apply();
			
			expect(WcAlertConsoleService.addMessage).toHaveBeenCalled();
			
			
		});

	});
	
});