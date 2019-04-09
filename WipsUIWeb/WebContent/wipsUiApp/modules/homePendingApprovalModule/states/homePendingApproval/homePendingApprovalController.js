'use strict';

angular.module('WipsUiApp.HomePendingApprovalModule')
	.controller('HomePendingApprovalController', ['$scope','$rootScope','SwitchJobFactory','SwitchATPJobCodeModalService','$state','User','_','SwitchJobServices','WcDataTableService','WcAlertConsoleService','resolvedATPApproval','SessionServices', 'WcHttpRequestService','WipsUtilityServices',	
		function($scope,$rootScope,SwitchJobFactory,SwitchATPJobCodeModalService,$state,User,_,SwitchJobServices,WcDataTableService,WcAlertConsoleService,resolvedATPApproval,SessionServices, WcHttpRequestService,WipsUtilityServices) {

		$scope.wipsUiAppController.isMenuVislbe = $state.current.name;
		
		this.ifAltJobCodeListTrue = User.userInformation.delegatedJobs;
		
		WcHttpRequestService.configuration.headers.Authorization = User.userInformation.ltermToken 
		
		this.ltermToken = User.userInformation.ltermToken;
		/*this.switchJobCode=function(){
			
			return SwitchJobFactory.switchJobCodes();
		}*/
this.switchJobCode=function(){
	$state.get('atp-pending-approval').data.clickedBackButtonCallNewData = "";
	$state.get('lump-sum-pending-approval').data.clickedBackButtonCallNewData = "";
	$state.get('price-claim-pending-approval').data.clickedBackButtonCallNewData = "";
			
			SwitchJobFactory.switchJobCodes().then(angular.bind(this,function(){

			}));
			
			
		}
		
		/*this.switchJobCode = function() {
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
					}
					User.setDelegatedJobs(altJobCodeList);
					
				}),
					function(altJobCodeListfailure) {
					this.homePendingApprovalTable = altJobCodeListfailure.atpListForApproval;
					this.atpPendingApproval = altJobCodeListfailure;
				});
			}));
		};*/
		

		this.homePendingApprovalTable = {};
	
		
		if($state.current.data.clickedBackButtonCallNewData == '' && resolvedATPApproval == undefined) {
		
			if(User.userInformation.httpResponseCode == '400'){
				WcAlertConsoleService.addMessage({
					message:User.userInformation.responseMessage,
					type: 'danger',
					multiple: false
				});
				this.homePendingApprovalTable = User.userInformation.pendingApprovals;
			}else{
				this.homePendingApprovalTable = User.userInformation.pendingApprovals;
				this.ltermToken = User.userInformation.ltermToken
			}
			this.homePendingApproval = User.userInformation.jobDetail;
		}else if($state.current.data.clickedBackButtonCallNewData == '' && resolvedATPApproval != undefined) {
			this.ltermToken = User.userInformation.ltermToken
			if(resolvedATPApproval.httpResponseCode == '400'){
				WcAlertConsoleService.addMessage({
					message:resolvedATPApproval.responseMessage,
					type: 'danger',
					multiple: false
				});
				this.homePendingApprovalTable = resolvedATPApproval.pendingApprovals;
			}else{
				this.homePendingApprovalTable = resolvedATPApproval.pendingApprovals;
				
				
			}
			this.homePendingApproval = resolvedATPApproval.jobDetail;
		}else if($state.current.data.clickedBackButtonCallNewData.errorFlag == false){
			
			this.atpPendingApproval = $state.current.data.clickedBackButtonCallNewData.userProfile;
			this.homePendingApprovalTable = $state.current.data.clickedBackButtonCallNewData.userProfile.atpListForApproval;
			this.ltermToken = User.userInformation.ltermToken
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

			$state.current.data.clickedBackButtonCallNewData = "";
			
		}else{
			this.atpPendingApproval = $state.current.data.clickedBackButtonCallNewData;
			this.homePendingApprovalTable = $state.current.data.clickedBackButtonCallNewData.atpListForApproval;
			this.ltermToken = User.userInformation.ltermToken
			$state.current.data.clickedBackButtonCallNewData = "";
		}


		this.readUnReadFlag = function (nTd, sData, oData, iRow, iCol) {
			if(oData.readOrUnreadFlag == "0"){
				$(nTd).addClass('read-flag');
			}else{
				$(nTd).addClass('un-read-flag');
			}
	      };

		this.homePendingApprovalTable.overrideOptions = {
				'bPaginate': false,
				'bInfo':false,
				"bSort": false,
				"oLanguage": {"sEmptyTable": "No items pending for approval"}
		};
		
		this.homePendingApprovalTable.columns = [{
			'mData': '',
			'aTargets': [0],
			'sWidth' : '33%',
			'sDefaultContent':'',
			"fnCreatedCell": this.readUnReadFlag,
		     mRender: function() {
		    	  var displayCategoryCode = arguments[2];
		            if(displayCategoryCode.categoryCode == 25){
		            	return '<a class="read test" href="javascript:;" ng-click="homePendingApprovalController.viewPendingApprovalList()">View List</a>';
		            }else if(displayCategoryCode.categoryCode == 102){
		            	return '<a class="read test" href="javascript:;" ng-click="homePendingApprovalController.viewLumpSumPendingApprovalList()">View List</a>';
		            }else if(displayCategoryCode.categoryCode == 3){
		            	return '<a class="read test" href="javascript:;" ng-click="homePendingApprovalController.viewPriceClaimPendingApprovalList()">View List</a>';
		            }
		            
		        }
		}, {
			'mData': '',
			'aTargets': [1],
			'sWidth' : '33%',
			"fnCreatedCell": this.readUnReadFlag,
		     mRender: function() {
		            var pendingApprovals = arguments[2];
		            return pendingApprovals.categoryName;
		    },
			'sDefaultContent':''
		}, {
			'mData': 'totalCount',
			'aTargets': [2],
			'sWidth' : '33%',
			"fnCreatedCell": this.readUnReadFlag,
			'sDefaultContent':''
		}];
		
		this.homePendingApprovalTable.columnDefs = [{
			'bSortable': false,
			'aTargets': [0,1,2]
		}];
		
		this.viewPendingApprovalList = function(){
			WcDataTableService.getClickedRowData().then(angular.bind(this, function(data) {
				User.setCategoryCode(data.categoryCode);
				var categoryCode = data.categoryCode;
				var param = {
						categoryCode: categoryCode,
						ltermToken:User.userInformation.ltermToken
				};
			$state.go('atp-pending-approval',param);
			}));
		};

		this.viewLumpSumPendingApprovalList = function(){
			WcDataTableService.getClickedRowData().then(angular.bind(this, function(data) {
				User.setCategoryCode(data.categoryCode);
				var categoryCode = data.categoryCode;
				var param = {
						categoryCode: categoryCode,
						ltermToken:User.userInformation.ltermToken
				};
			$state.go('lump-sum-pending-approval',param);
			}));
		};	
		this.viewPriceClaimPendingApprovalList = function(){
			WcDataTableService.getClickedRowData().then(angular.bind(this, function(data) {
				User.setCategoryCode(data.categoryCode);
				var categoryCode = data.categoryCode;
				var param = {
						categoryCode: categoryCode,
						ltermToken:User.userInformation.ltermToken
				};
			$state.go('price-claim-pending-approval',param);
			}));
		};

		
	}]);
