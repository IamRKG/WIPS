'use strict';

angular.module('WipsUiApp.ComponentsModule').
	service('User',['$window',function($window){

		this.userInformation = {};
		this.currentjobDetail = {};
		this.currentCategoryCode={};
		
	    this.setCachedUserInfo = function(user) {
			$window.sessionStorage.user = JSON.stringify(user);
			this.userInformation = JSON.parse($window.sessionStorage.user);

			$window.sessionStorage.currentjobDetail = JSON.stringify(user.jobDetail);
			this.currentjobDetail = JSON.parse($window.sessionStorage.currentjobDetail);
		};

		this.setDelegatedJobs = function(DelegatedJobs) {
			$window.sessionStorage.currentjobDetail = JSON.stringify(DelegatedJobs.jobDetail);
			this.currentjobDetail = JSON.parse($window.sessionStorage.currentjobDetail);
		};
		this.setCategoryCode = function(categoryCode) {
			$window.sessionStorage.currentCategoryCode = categoryCode;
			this.currentCategoryCode = JSON.parse($window.sessionStorage.currentCategoryCode);
		};

	this.getClearUserInformation = function (){
			$window.sessionStorage.clear();
			this.userInformation = undefined;
			this.currentjobDetail = undefined;
			this.currentCategoryCode = undefined;
		};
}]);