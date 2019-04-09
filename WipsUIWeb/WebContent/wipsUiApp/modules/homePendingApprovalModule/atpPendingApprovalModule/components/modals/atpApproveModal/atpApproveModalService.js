angular.module('WipsUiApp.ATPPendingApproval.ComponentsModule')
	.service('atpApproveModalService', ['$modal','User',function($modal,User) {
		this.openATPApproveModal = function(approvalResponse) {

			var atpApproveModalModalInstance = $modal.open({
				templateUrl: './wipsUiApp/modules/homePendingApprovalModule/atpPendingApprovalModule/components/modals/atpApproveModal/atpApproveModalTemplate.html',
				controller: 'ATPApproveModalInstanceController as atpApproveModalInstanceController',
				resolve: {
					approvalMessage: function () {
			          return approvalResponse;
			        }
			      }
			});
			
			return atpApproveModalModalInstance.result.then(function(selectedConfirm){
				this.selectedConfirm = selectedConfirm;
				return selectedConfirm;
			});
		};

	}]);