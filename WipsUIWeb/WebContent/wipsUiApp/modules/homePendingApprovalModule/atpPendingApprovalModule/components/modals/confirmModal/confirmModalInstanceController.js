'use strict';

angular.module('WipsUiApp.ATPPendingApproval.ComponentsModule')
	.controller('ConfirmModalInstanceController', ['$modalInstance','displayInfoResponse',function ($modalInstance,displayInfoResponse) {

	this.selectedButtonValue = displayInfoResponse;	
	
	this.resolve = function () {
		$modalInstance.close();
	};

	this.reject = function () {
		$modalInstance.dismiss();
	};
}]);