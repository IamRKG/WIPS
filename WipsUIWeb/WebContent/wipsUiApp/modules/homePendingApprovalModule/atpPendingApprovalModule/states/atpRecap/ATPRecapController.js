'use strict';

angular.module('WipsUiApp.ATPPendingApprovalModule')
	.controller('ATPRecapController', ['$scope','resolvedATPRecap','AtpRecapPrototype','SwitchJobServices','User','$state','ATPApproveServices','atpApproveModalService','SessionServices','WcAlertConsoleService','confirmModalService','ViewAttachmentDetailsServices','_',
		function($scope,resolvedATPRecap,AtpRecapPrototype,SwitchJobServices,User,$state,ATPApproveServices,atpApproveModalService,SessionServices,WcAlertConsoleService,confirmModalService,ViewAttachmentDetailsServices,_) {
		document.body.scrollTop =0;
		this.atpRecap = new AtpRecapPrototype(resolvedATPRecap.g53xTransactionOutput);
		this.attachmentsErrorFlag = resolvedATPRecap.attachmentsErrorFlag;
		this.attachmentsErrorMessage = resolvedATPRecap.attachmentsErrorMessage;
		this.jobDetail = User.currentjobDetail;
	
		this.getPrevious = function(){
			/*TODO: Need to verify which logic below code(if/else part) is require*/ 
			var param = {
					categoryCode: User.currentCategoryCode,
					ltermToken:User.userInformation.ltermToken
			};
			
			$state.go('atp-pending-approval',param);
			};
	

		
		this.atpRemarks = function(){
				var param = {
					ltermToken:User.userInformation.ltermToken,
					atpNumber: this.atpRecap.atpNumber
				};
				$state.go('atp-remarks',param);
		};
		
		this.atpStrategyDetails = function(){
			var param = {
					ltermToken:User.userInformation.ltermToken,
					atpNumber:this.atpRecap.atpNumber

			};
			$state.go('atp-strategy-details',param);
	};
		
		var param = {
				atpNumber : this.atpRecap.atpNumber,
				actionTaken :'',
				ltermToken:User.userInformation.ltermToken
		};
		
		this.getATPApprove = function(){
			
			return confirmModalService.openConfirmModal('Approve').then(angular.bind(this,function(){
				param.actionTaken = 'Approve';

			return ATPApproveServices.getATPApprove(param).then(angular.bind(this, function(approvalResponse) {

				if(approvalResponse.errorFlag == true){
					
					return atpApproveModalService.openATPApproveModal(approvalResponse).then(angular.bind(this,function(selectedConfirm){
						
						param.actionTaken = selectedConfirm;
						param.ltermToken = User.userInformation.ltermToken;
						
						return ATPApproveServices.getATPApprove(param).then(angular.bind(this, function(approvalResponse) {
							
							/*TODO:Movde to function below code*/
							$state.get('atp-pending-approval').data.clickedBackButtonCallNewData = approvalResponse;
							$state.go('atp-pending-approval');
							
						}));
						
					}));
					
				}else{
					$state.get('atp-pending-approval').data.clickedBackButtonCallNewData = approvalResponse;
					$state.go('atp-pending-approval');
				}
			}));
			}));
		};
		
		this.getATPReject = function(){
			return confirmModalService.openConfirmModal('Reject').then(angular.bind(this,function(){
				param.ltermToken = User.userInformation.ltermToken;
				param.actionTaken = 'Reject';
	
				return ATPApproveServices.getATPApprove(param).then(angular.bind(this, function(rejectResponse) {
					$state.get('atp-pending-approval').data.clickedBackButtonCallNewData = rejectResponse;
					$state.go('atp-pending-approval');
				}));
			}));
		};

		this.viewAttachmentDetails = function (attachmentDetails) {
			console.log(attachmentDetails.id);
			this.attachmentId = _.findWhere(this.atpRecap.attachmentDetails, {id: attachmentDetails.id})
			console.log(this.attachmentId.id);
			ViewAttachmentDetailsServices.getviewAttachmentDetails(this.attachmentId.id);
		};

	}]);
