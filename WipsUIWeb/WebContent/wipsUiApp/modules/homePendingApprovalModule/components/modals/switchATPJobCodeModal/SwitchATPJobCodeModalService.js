angular.module('WipsUiApp.HomePendingApproval.ComponentsModule')
	.service('SwitchATPJobCodeModalService', ['$modal','User',function($modal,User) {
		this.openSwitchATPJobCodeModal = function() {

			var switchATPJobCodeModalInstance = $modal.open({
				templateUrl: './wipsUiApp/modules/homePendingApprovalModule/components/modals/switchATPJobCodeModal/switchATPJobCodeModalTemplate.html',
				controller: 'SwitchATPJobCodeModalInstanceController as switchATPJobCodeModalInstanceController',
				resolve: {
					jobDetail: function () {
			          return User.userInformation.jobDetail;
					},
					delegatedJobs: function () {
			          return User.userInformation.delegatedJobs;
			        }
			      }
			});
			
			return switchATPJobCodeModalInstance.result.then(angular.bind(this,function(selectedAltJobCodeList){
				this.selectedAltJobCodeList = selectedAltJobCodeList;
				return selectedAltJobCodeList;
			}));
		};

	}]);