angular.module('WipsUiApp.ATPPendingApproval.ComponentsModule')
	.service('confirmModalService', ['$modal',function($modal) {
		this.openConfirmModal = function(response) {

			var confirmModalInstance = $modal.open({
				templateUrl: './wipsUiApp/modules/homePendingApprovalModule/atpPendingApprovalModule/components/modals/confirmModal/confirmModalTemplate.html',
				controller: 'ConfirmModalInstanceController as confirmModalInstanceController',
				resolve:{
					displayInfoResponse:function(){
						
						return response;
						
					}
				}
			});
			
			return confirmModalInstance.result;

		};

	}]);