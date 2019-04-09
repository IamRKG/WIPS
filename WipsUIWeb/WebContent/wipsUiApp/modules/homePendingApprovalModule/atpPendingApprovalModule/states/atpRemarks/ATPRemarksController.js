'use strict';

angular.module('WipsUiApp.ATPPendingApprovalModule')
	.controller('ATPRemarksController', ['$scope','resolvedATPRemarks','AtpRemarksPrototype','User','ATPRemarksServices','_','$state','ATPApproveServices','atpApproveModalService','WcAlertConsoleService','SessionServices','confirmModalService',
		function($scope,resolvedATPRemarks,AtpRemarksPrototype,User,ATPRemarksServices,_,$state,ATPApproveServices,atpApproveModalService,WcAlertConsoleService,SessionServices,confirmModalService) {

		this.atpRemarks = new AtpRemarksPrototype(resolvedATPRemarks.remarks);

		this.jobDetail = User.currentjobDetail;



		var atpRemarksListName =  _.findWhere(this.atpRemarks.remark, {userJobCode:this.jobDetail.jobCode});

		if(atpRemarksListName == undefined){
			this.selectedRemarksNotes = '';
		}else{
			this.selectedRemarksNotes = atpRemarksListName.remarks;
		}


		this.ifMatchATPRemarksList = {};

		this.ifMatchATPRemarksList.userJobCode = this.jobDetail.jobCode;

		this.ifMatchATPRemarksList.buyerCode = this.atpRemarks.buyerCode;

		this.ifMatchATPRemarksList.buyerCodeUndefined = undefined;

		var param = {
			ltermToken:User.userInformation.ltermToken,
			atpNumber: this.atpRemarks.atpNumber,
			actionTaken :''
		};


		this.save = function(){
			if(this.selectedRemarksNotes.length!=0){
			param.userRemarks = this.selectedRemarksNotes;
			}

			return 	ATPRemarksServices.getSaveRemarks(param).then(angular.bind(this, function(userRemarks) {

				var atpRemarksListName =  _.findWhere(userRemarks.remarks.remark, {userJobCode:User.currentjobDetail.jobCode});

				if(atpRemarksListName == undefined){
					this.selectedRemarksNotes = '';
				}else{

					this.selectedRemarksNotes = atpRemarksListName.remarks;
					if(userRemarks.remarks.remarksSuccessfullySaved == true) {
						WcAlertConsoleService.addMessage({
							message: 'Saved!',
							type: 'success',
							multiple: false
						});
					}else{
						WcAlertConsoleService.addMessage({
							message: 'Could not save remarks!',
							type: 'danger',
							multiple: false
						});
					}
				}

			}));

		};


	this.getATPApprove = function(){
		return confirmModalService.openConfirmModal('Approve').then(angular.bind(this,function(){
			param.actionTaken = 'Approve';
			param.ltermToken = User.userInformation.ltermToken;
			   if(atpRemarksListName.remarks != this.selectedRemarksNotes){
				   param.userRemarks = this.selectedRemarksNotes;
				   return ATPApproveServices.getSaveRemarksAndApproveOrRejectAtp(param).then(angular.bind(this, function(approvalResponse) {
					   if(approvalResponse.errorFlag == true){
						   return atpApproveModalService.openATPApproveModal(approvalResponse).then(angular.bind(this,function(selectedConfirm){
							   param.actionTaken = selectedConfirm;
							   return ATPApproveServices.getATPApprove(param).then(angular.bind(this, function(approvalResponse) {
								   this.navigateToAtpPendingApprova(approvalResponse);
							   }));

						   }));
					   }else{
						   this.navigateToAtpPendingApprova(approvalResponse);
					   }
				   }));
			   }else{
				   return ATPApproveServices.getATPApprove(param).then(angular.bind(this, function(approvalResponse) {
					   if(approvalResponse.errorFlag == true){
						   return atpApproveModalService.openATPApproveModal(approvalResponse).then(angular.bind(this,function(selectedConfirm){
							   param.actionTaken = selectedConfirm;
							   param.ltermToken = User.userInformation.ltermToken;
							   return ATPApproveServices.getATPApprove(param).then(angular.bind(this, function(approvalResponse) {
								   this.navigateToAtpPendingApprova(approvalResponse);
							   }));
						   }));
					   }else{
						   this.navigateToAtpPendingApprova(approvalResponse);
					   }
				   }));
			   }


		}));
	};

	this.getATPReject = function(){
		return confirmModalService.openConfirmModal('Reject').then(angular.bind(this,function(){
			param.actionTaken = 'Reject';
			param.ltermToken = User.userInformation.ltermToken;
			if(this.selectedRemarksNotes.length!=0){
			param.userRemarks = this.selectedRemarksNotes;
			}
			if(atpRemarksListName.remarks != this.selectedRemarksNotes){
				return ATPApproveServices.getSaveRemarksAndApproveOrRejectAtp(param).then(angular.bind(this, function(approvalResponse) {
					this.navigateToAtpPendingApprova(approvalResponse);
				}));
			}else{
				return ATPApproveServices.getATPApprove(param).then(angular.bind(this, function(approvalResponse) {
					this.navigateToAtpPendingApprova(approvalResponse);
				}));
			}

		}));
		};

			this.getPrevious = function(){
				param.ltermToken = User.userInformation.ltermToken;
				$state.go('atp-recap',param);
			};


			this.clear = function(){
				this.selectedRemarksNotes = [];
			};

			this.navigateToAtpPendingApprova = function(approvalResponse){
				$state.get('atp-pending-approval').data.clickedBackButtonCallNewData = approvalResponse;
				$state.go('atp-pending-approval');

			}

		}]);
