'use strict';

angular.module('WipsUiApp.ATPPendingApprovalModule')
	.controller('ATPPendingApprovalController', ['$scope','$state','User','SwitchJobFactory','WcDataTableService','WcAlertConsoleService','resolvedATPApproval',
		function($scope,$state,User,SwitchJobFactory,WcDataTableService,WcAlertConsoleService,resolvedATPApproval) {
		this.ifAltJobCodeListTrue = User.userInformation.delegatedJobs;
			this.atpRecap = function(){
				WcDataTableService.getClickedRowData().then(angular.bind(this, function(data) {
					var atpRecapNumber = data.entityNumber;
					var param = {
						ltermToken:User.userInformation.ltermToken,
						atpNumber: atpRecapNumber
					};
					$state.go('atp-recap',param);

				}));
			};
		this.switchJobCode=function(){
			$state.get('atp-pending-approval').data.clickedBackButtonCallNewData = "";
			$state.get('lump-sum-pending-approval').data.clickedBackButtonCallNewData = "";
			$state.get('price-claim-pending-approval').data.clickedBackButtonCallNewData = "";
			SwitchJobFactory.switchJobCodes();
		}
			if ($state.current.data.clickedBackButtonCallNewData == ''){
				this.atpPendingApprovalTable = resolvedATPApproval.atp;
				this.jobDetail = User.currentjobDetail;
			}else{
			if($state.current.data.clickedBackButtonCallNewData.errorFlag == false){
				this.atpPendingApprovalTable = $state.current.data.clickedBackButtonCallNewData.atp;
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
				}
			}else{
				return undefined;/*handling in services using 409 error status code*/
			}
			}
	
		this.atpPendingApprovalTable.overrideOptions = {
				'bPaginate': false,
				'bInfo':false,
				"bSort": false
		};
		
		this.atpPendingApprovalTable.columns = [{
			'mData': '',
			'aTargets': [0],
			'sWidth' : '33%',
			'sDefaultContent':'',
		     mRender: function() {
		            var report = arguments[2];
		            return '<a class="read" href="javascript:;" ng-click="atpPendingApprovalController.atpRecap()">'+report.entityNumber+'</a>';
		        }
		}, {
			'mData': 'partNumber',
			'aTargets': [1],
			'sWidth' : '33%',
			'sDefaultContent':''
		}, {
			'mData': 'buyerCode',
			'aTargets': [2],
			'sWidth' : '33%',
			'sDefaultContent':''
		}];
		
		this.atpPendingApprovalTable.columnDefs = [{
			'bSortable': false,
			'aTargets': [0,1,2]
		}];
	}]);
