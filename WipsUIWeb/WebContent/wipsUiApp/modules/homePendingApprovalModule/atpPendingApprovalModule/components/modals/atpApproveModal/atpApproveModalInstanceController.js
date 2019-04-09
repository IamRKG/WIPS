'use strict';

angular.module('WipsUiApp.ATPPendingApproval.ComponentsModule')
	.controller('ATPApproveModalInstanceController', ['$modalInstance','approvalMessage',function ($modalInstance,approvalMessage) {

	this.approvalWarningMessage = approvalMessage;
	
	this.currentStep = this.approvalWarningMessage.warningMessagesList[0];
	
	this.nextStep = function(actionTakenMessage,index) {
		if(actionTakenMessage == 'Confirm'){
			for(var i = 0; i <= index; i++){
				this.currentStep = this.approvalWarningMessage.warningMessagesList[i+1];
			}
		}else{
			var actionPerformed =  actionTakenMessage.replace('Approve','Confirm');
			$modalInstance.close(actionPerformed);
		}
	    };

	this.reject = function () {
		$modalInstance.dismiss();
	};
}]);