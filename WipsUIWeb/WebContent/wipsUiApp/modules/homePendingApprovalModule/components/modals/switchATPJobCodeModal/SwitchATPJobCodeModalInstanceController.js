'use strict';

angular.module('WipsUiApp.HomePendingApproval.ComponentsModule')
	.controller('SwitchATPJobCodeModalInstanceController', ['$modalInstance','delegatedJobs','jobDetail','SwitchJobServices','SwitchATPJobCodeModalService','$scope','User',function ($modalInstance,delegatedJobs,jobDetail,SwitchJobServices,SwitchATPJobCodeModalService,$scope,User) {
	this.availableDelegatedJobs = delegatedJobs;
	this.userJob = jobDetail;
	this.currentJobcodeDisable=function(getJobcode){
		if(User.currentjobDetail.jobCode==getJobcode.jobCode){
			return true;
		}
		else{
			return false;
		}
	}
	$scope.form = {};
	
	this.resolve = function () {
		if($scope.form.altJobCodeForm.$valid){
			$modalInstance.close(this.delegatedJobs);
		}else{
			this.isFormSubmitted = true;
		}
	};

	this.reject = function () {
		$modalInstance.dismiss();
	};
}]);