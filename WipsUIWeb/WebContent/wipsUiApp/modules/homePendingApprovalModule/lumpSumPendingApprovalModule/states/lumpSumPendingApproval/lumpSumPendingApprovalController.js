'use strict';

angular.module('WipsUiApp.LumpSumPendingApprovalModule')
	.controller('LumpSumPendingApprovalController', ['$scope','$state','SwitchJobFactory','User','WcDataTableService','WcAlertConsoleService','resolvedLumpSumApprovalList',
		function($scope,$state,SwitchJobFactory,User,WcDataTableService,WcAlertConsoleService,resolvedLumpSumApprovalList) {
			
			/*****Click Lump Sum No and go to detail page*****/
			this.ifAltJobCodeListTrue = User.userInformation.delegatedJobs;
			this.lumpSumDetailsApprovals = function(){
				WcDataTableService.getClickedRowData().then(angular.bind(this, function(data) {
					this.lumpsumNumber = data.entityNumber;
					this.defaultAmendment='00';
					var param = {
						ltermToken:User.userInformation.ltermToken,
						lumpsumNumber: this.lumpsumNumber,
						defaultAmendment:this.defaultAmendment
					};
					$state.go('lump-sum-details-approvals',param);
				}));
			};
this.switchJobCode=function(){
	$state.get('atp-pending-approval').data.clickedBackButtonCallNewData = "";
	$state.get('lump-sum-pending-approval').data.clickedBackButtonCallNewData = "";
	$state.get('price-claim-pending-approval').data.clickedBackButtonCallNewData = "";
    			SwitchJobFactory.switchJobCodes();
    			
    		
    		}
		if ($state.current.data.clickedBackButtonCallNewData == ''){
			this.lumpSumPendingApprovalTable = resolvedLumpSumApprovalList.lumpSum;
			this.jobDetail = User.currentjobDetail;
		}else{
			if($state.current.data.clickedBackButtonCallNewData.errorFlag == false){
				this.lumpSumPendingApprovalTable = $state.current.data.clickedBackButtonCallNewData.lumpSum;
				this.jobDetail = User.currentjobDetail;
				if($state.current.data.clickedBackButtonCallNewData.actionAlreadyTakenFlag==true){
					WcAlertConsoleService.addMessage({
						message:$state.current.data.clickedBackButtonCallNewData.approveOrRejectMessage,
						type: 'danger',
						multiple: false
					});
				}else{
					WcAlertConsoleService.addMessage({
						message:$state.current.data.clickedBackButtonCallNewData.approveOrRejectMessage,
						type: 'success',
						multiple: false
					});
				}}else{
				return undefined;/*handling in services using 409 error status code*/
			}
		}

		this.lumpSumPendingApprovalTable.overrideOptions = {
				'bPaginate': false,
				'bInfo':false,
				"bSort": false
		};

		this.lumpSumPendingApprovalTable.columns = [{
			'mData': '',
			'aTargets': [0],
			'sWidth' : '25%',
			'sDefaultContent':'',
		     mRender: function() {
		            var report = arguments[2];
		            return '<a class="read" href="javascript:;" ng-click="lumpSumPendingApprovalController.lumpSumDetailsApprovals();">'+report.entityNumber+'</a>';
		        }
		}, {
			'mData': 'buyerCode',
			'aTargets': [1],
			'sWidth' : '25%',
			'sDefaultContent':''
		}, {
			'mData': 'cause',
			'aTargets': [2],
			'sWidth' : '25%',
			'sDefaultContent':''
		},{
			'mData': 'supplierCode',
			'aTargets': [3],
			'sWidth' : '25%',
			'sDefaultContent':''
		}];

		this.lumpSumPendingApprovalTable.columnDefs = [{
			'bSortable': false,
			'aTargets': [0,1,2]
		}];

	}]);
