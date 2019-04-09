'use strict';

angular.module('WipsUiApp.priceClaimPendingApprovalModule')
    .controller('PriceClaimPendingApprovalController', ['$scope','SwitchJobFactory','$rootScope', '$state', 'User', 'WcDataTableService', 'WcAlertConsoleService', 'resolvedPriceClaimApprovalList',
        function ($scope,SwitchJobFactory,$rootScope, $state, User, WcDataTableService, WcAlertConsoleService, resolvedPriceClaimApprovalList) {

    	this.ifAltJobCodeListTrue = User.userInformation.delegatedJobs;
            this.priceClaimDetailsApprovals = function () {
                WcDataTableService.getClickedRowData().then(angular.bind(this, function (data) {
                    var priceClaimRecapNumber = data.entityNumber;
                    var supplier = data.supplier;
                    var param = {
                        ltermToken: User.userInformation.ltermToken,
                        priceClaimNumber: priceClaimRecapNumber,
                        supplier: supplier
                    };
                    $state.go('price-claim-details-approvals', param);
                }));
            };

            this.switchJobCode=function(){
            	$state.get('atp-pending-approval').data.clickedBackButtonCallNewData = "";
    			$state.get('lump-sum-pending-approval').data.clickedBackButtonCallNewData = "";
    			$state.get('price-claim-pending-approval').data.clickedBackButtonCallNewData = "";
    			SwitchJobFactory.switchJobCodes();
    			
    		
    		}
            if ($state.current.data.clickedBackButtonCallNewData == '') {
                this.priceClaimPendingApprovalTable = resolvedPriceClaimApprovalList.priceClaims;
                this.jobDetail = User.currentjobDetail;
            } else {
                if ($state.current.data.clickedBackButtonCallNewData.errorFlag == false) {
                    this.priceClaimPendingApprovalTable = $state.current.data.clickedBackButtonCallNewData.priceClaims;
                    this.jobDetail = User.currentjobDetail;
                    if ($state.current.data.clickedBackButtonCallNewData.actionAlreadyTakenFlag == true) {
                        WcAlertConsoleService.addMessage({
                            message: $state.current.data.clickedBackButtonCallNewData.approveOrRejectMessage,
                            type: 'danger',
                            multiple: false
                        });
                    } else {
                        WcAlertConsoleService.addMessage({
                            message: $state.current.data.clickedBackButtonCallNewData.approveOrRejectMessage,
                            type: 'success',
                            multiple: false
                        });
                    }
                }
                else {
                    return undefined;/*handling in services using 409 error status code*/
                }
            }

            this.priceClaimPendingApprovalTable.overrideOptions = {
                'bPaginate': false,
                'bInfo': false,
                "bSort": false
            };

            this.priceClaimPendingApprovalTable.columns = [{
                'mData': '',
                'aTargets': [0],
                'sWidth': '25%',
                'sDefaultContent': '',
                mRender: function () {
                    var report = arguments[2];
                    return '<a class="read" href="javascript:;" ng-click="priceClaimPendingApprovalController.priceClaimDetailsApprovals();">' + report.entityNumber + '</a>';
                }
            }, {
                'mData': 'supplier',
                'aTargets': [1],
                'sWidth': '25%',
                'sDefaultContent': ''
            }, {
                'mData': 'effectiveDate',
                'aTargets': [2],
                'sWidth': '25%',
                'sDefaultContent': ''
            }, {
                'mData': 'claimTitle',
                'aTargets': [3],
                'sWidth': '25%',
                'sDefaultContent': ''
            }];

            this.priceClaimPendingApprovalTable.columnDefs = [{
                'bSortable': false,
                'aTargets': [0, 1, 2]
            }];

        }]);
