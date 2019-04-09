angular.module('WipsUiApp.HomePendingApproval.ComponentsModule')
	.service('SwitchJobFactory', ['SwitchATPJobCodeModalService','WcAlertConsoleService','SwitchJobServices','WipsUtilityServices','$state','User',function(SwitchATPJobCodeModalService,WcAlertConsoleService,SwitchJobServices,WipsUtilityServices,$state,User) {
			this.switchJobCodes = function() {
				this.ltermToken = User.userInformation.ltermToken
			return SwitchATPJobCodeModalService.openSwitchATPJobCodeModal().then(angular.bind(this, function(selectedAltJobCodeList) {
				WcAlertConsoleService.removeErrorMessages();
				this.selectedAltJobCodeList = selectedAltJobCodeList;
				this.jobCode = this.selectedAltJobCodeList.jobCode;
				this.jobName = this.selectedAltJobCodeList.jobName;
				var param = {
						jobCode: this.jobCode,
						jobName: this.jobName,
						ltermToken:this.ltermToken
				};
				return 	SwitchJobServices.getJobCodeList(param).then(angular.bind(this, function(altJobCodeList) {
					if(altJobCodeList.pendingApprovals.length==1){
						WipsUtilityServices.redirectiveListPage(altJobCodeList);
					}else{
						
						this.homePendingApprovalTable = altJobCodeList.pendingApprovals;
						this.homePendingApproval = altJobCodeList.jobDetail;
						$state.go('home-atp-pending-approval',{},{reload:true});
					}
					User.setDelegatedJobs(altJobCodeList);
					
				}),
					function(altJobCodeListfailure) {
					this.homePendingApprovalTable = altJobCodeListfailure.atpListForApproval;
					this.atpPendingApproval = altJobCodeListfailure;
				});
			}));
		};
		this.homePendingApprovalTable = '';
		this.homePendingApproval = '';
		
	}]);